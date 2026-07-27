import MarkdownIt from "markdown-it";

function normalizeSlug(value: string) {
  return (
    value
      .trim()
      .toLowerCase()
      .replace(/[^\p{Letter}\p{Number}\s-]/gu, "")
      .replace(/\s+/g, "-")
      .replace(/-+/g, "-") || "section"
  );
}

function slugify(value: string, slugCounts: Map<string, number>) {
  const base =
    normalizeSlug(value);
  const count = slugCounts.get(base) ?? 0;
  slugCounts.set(base, count + 1);
  return count === 0 ? base : `${base}-${count + 1}`;
}

export function buildHeadingAnchors(headings: string[]) {
  const slugCounts = new Map<string, number>();
  return headings.map((title) => ({
    title,
    id: slugify(title, slugCounts)
  }));
}

export function renderMarkdown(content: string) {
  const slugCounts = new Map<string, number>();

  const md = new MarkdownIt({
    html: false,
    linkify: true,
    breaks: false
  });

  const defaultHeadingOpen =
    md.renderer.rules.heading_open ??
    ((tokens, idx, options, _env, self) => self.renderToken(tokens, idx, options));

  md.renderer.rules.heading_open = (tokens, idx, options, env, self) => {
    const title = tokens[idx + 1]?.content ?? "";
    tokens[idx].attrSet("id", slugify(title, slugCounts));
    return defaultHeadingOpen(tokens, idx, options, env, self);
  };

  const defaultLinkOpen =
    md.renderer.rules.link_open ??
    ((tokens, idx, options, _env, self) => self.renderToken(tokens, idx, options));

  md.renderer.rules.link_open = (tokens, idx, options, env, self) => {
    const href = tokens[idx].attrGet("href");
    if (href?.startsWith("http")) {
      tokens[idx].attrSet("target", "_blank");
      tokens[idx].attrSet("rel", "noreferrer");
    }
    return defaultLinkOpen(tokens, idx, options, env, self);
  };

  return md.render(content);
}
