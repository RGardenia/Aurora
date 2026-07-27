import { createHash } from "node:crypto";
import { cp, mkdir, readFile, rm, stat, writeFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const serviceDir = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "..");
const repoRoot = path.resolve(serviceDir, "..");
const outputDir = path.join(serviceDir, "public", "content");
const docsDir = path.join(outputDir, "docs");
const assetsDir = path.join(outputDir, "assets");

const knowledgeRoots = [
  "Algorithm",
  "BigData",
  "CloudNative",
  "Computer",
  "DataBase",
  "DesultoryEssay",
  "Interview",
  "Languages",
  "MachineLearning",
  "Middleware",
  "SuperComputing",
  "WEB"
];

const ignoredDirs = new Set([
  ".git",
  ".idea",
  ".obsidian",
  "node_modules",
  "dist",
  "target",
  "service"
]);

const assetExtensions = new Set([
  ".png",
  ".jpg",
  ".jpeg",
  ".gif",
  ".webp",
  ".svg",
  ".bmp",
  ".ico"
]);

function safeDecodeUri(value) {
  try {
    return decodeURIComponent(value);
  } catch {
    return value;
  }
}

function toPosix(value) {
  return value.split(path.sep).join("/");
}

function hash(value) {
  return createHash("sha1").update(value).digest("hex").slice(0, 16);
}

function titleFromMarkdown(markdown, filePath) {
  const heading = markdown.match(/^#\s+(.+)$/m)?.[1]?.trim();
  if (heading) return heading.replace(/[#*_`]/g, "").trim();
  const base = path.basename(filePath, path.extname(filePath));
  return base === "README" ? path.basename(path.dirname(filePath)) : base;
}

function summaryFromMarkdown(markdown) {
  const line = markdown
    .split(/\r?\n/)
    .map((item) => item.trim())
    .find((item) => item && !item.startsWith("#") && !item.startsWith("!") && !item.startsWith(">"));
  return (line ?? "").replace(/[`*_#[\]]/g, "").slice(0, 180);
}

function headingsFromMarkdown(markdown) {
  return [...markdown.matchAll(/^#{1,3}\s+(.+)$/gm)]
    .map((match) => match[1].replace(/[#*_`]/g, "").trim())
    .filter(Boolean)
    .slice(0, 16);
}

function wordCount(markdown) {
  const compact = markdown
    .replace(/```[\s\S]*?```/g, " ")
    .replace(/!\[[^\]]*]\([^)]*\)/g, " ")
    .replace(/\[[^\]]*]\([^)]*\)/g, " ")
    .replace(/[^\p{Letter}\p{Number}\u4e00-\u9fa5]+/gu, " ");
  const latin = compact.match(/[A-Za-z0-9]+/g)?.length ?? 0;
  const chinese = compact.match(/[\u4e00-\u9fa5]/g)?.length ?? 0;
  return latin + chinese;
}

async function exists(filePath) {
  try {
    await stat(filePath);
    return true;
  } catch {
    return false;
  }
}

async function walk(dir, rootName, documents) {
  const entries = await import("node:fs/promises").then((fs) =>
    fs.readdir(dir, { withFileTypes: true })
  );

  for (const entry of entries) {
    if (entry.name.startsWith(".") || ignoredDirs.has(entry.name)) continue;
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      await walk(fullPath, rootName, documents);
      continue;
    }
    if (!entry.isFile() || path.extname(entry.name).toLowerCase() !== ".md") continue;

    const relativePath = toPosix(path.relative(repoRoot, fullPath));
    const raw = await readFile(fullPath, "utf8");
    const content = await rewriteAssets(raw, fullPath);
    const id = hash(relativePath);
    const doc = {
      id,
      title: titleFromMarkdown(raw, fullPath),
      path: relativePath,
      root: rootName,
      segments: relativePath.split("/").slice(1, -1),
      summary: summaryFromMarkdown(raw),
      headings: headingsFromMarkdown(raw),
      wordCount: wordCount(raw),
      updatedAt: (await stat(fullPath)).mtime.toISOString(),
      contentUrl: `/content/docs/${id}.json`
    };

    await writeFile(
      path.join(docsDir, `${id}.json`),
      JSON.stringify({ ...doc, content }, null, 2),
      "utf8"
    );
    documents.push(doc);
  }
}

async function rewriteAssets(markdown, markdownPath) {
  const assetPattern = /!\[([^\]]*)]\((?!https?:\/\/|data:|#)([^)\s]+)(?:\s+"[^"]*")?\)/g;
  const replacements = [];

  for (const match of markdown.matchAll(assetPattern)) {
    const alt = match[1];
    const target = safeDecodeUri(match[2]);
    if (target.startsWith("/")) continue;

    const source = path.resolve(path.dirname(markdownPath), target);
    const ext = path.extname(source).toLowerCase();
    if (!assetExtensions.has(ext) || !(await exists(source))) continue;

    const assetName = `${hash(toPosix(path.relative(repoRoot, source)))}${ext}`;
    const dest = path.join(assetsDir, assetName);
    await cp(source, dest);
    replacements.push({
      from: match[0],
      to: `![${alt}](/content/assets/${assetName})`
    });
  }

  return replacements.reduce((next, item) => next.replace(item.from, item.to), markdown);
}

await rm(outputDir, { recursive: true, force: true });
await mkdir(docsDir, { recursive: true });
await mkdir(assetsDir, { recursive: true });

const documents = [];
const roots = [];

for (const rootName of knowledgeRoots) {
  const rootPath = path.join(repoRoot, rootName);
  if (!(await exists(rootPath))) continue;
  const before = documents.length;
  await walk(rootPath, rootName, documents);
  roots.push({
    id: rootName,
    name: rootName,
    path: rootName,
    docCount: documents.length - before
  });
}

documents.sort((a, b) => a.path.localeCompare(b.path, "zh-Hans-CN"));
roots.sort((a, b) => a.name.localeCompare(b.name, "zh-Hans-CN"));

await writeFile(
  path.join(outputDir, "manifest.json"),
  JSON.stringify({ generatedAt: new Date().toISOString(), roots, documents }, null, 2),
  "utf8"
);

console.log(`Generated ${documents.length} markdown documents into ${toPosix(path.relative(repoRoot, outputDir))}`);
