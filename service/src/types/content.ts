export interface KnowledgeRoot {
  id: string;
  name: string;
  path: string;
  docCount: number;
}

export interface DocNode {
  id: string;
  title: string;
  path: string;
  root: string;
  segments: string[];
  summary: string;
  headings: string[];
  wordCount: number;
  updatedAt: string;
  contentUrl: string;
}

export interface ContentManifest {
  generatedAt: string;
  roots: KnowledgeRoot[];
  documents: DocNode[];
}

export interface BlogDocument extends DocNode {
  content: string;
}
