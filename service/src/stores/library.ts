import { defineStore } from "pinia";
import type { BlogDocument, ContentManifest, DocNode } from "@/types/content";

interface LibraryState {
  manifest: ContentManifest | null;
  currentDocument: BlogDocument | null;
  activeRoot: string;
  query: string;
  loading: boolean;
  documentLoading: boolean;
  error: string;
}

function scoreDocument(doc: DocNode, query: string) {
  const haystack = `${doc.title} ${doc.path} ${doc.summary} ${doc.headings.join(" ")}`.toLowerCase();
  const terms = query.toLowerCase().split(/\s+/).filter(Boolean);
  if (terms.length === 0) return 1;
  return terms.reduce((score, term) => (haystack.includes(term) ? score + 1 : score), 0);
}

export const useLibraryStore = defineStore("library", {
  state: (): LibraryState => ({
    manifest: null,
    currentDocument: null,
    activeRoot: "all",
    query: "",
    loading: false,
    documentLoading: false,
    error: ""
  }),

  getters: {
    documents(state): DocNode[] {
      return state.manifest?.documents ?? [];
    },
    roots(state) {
      return state.manifest?.roots ?? [];
    },
    filteredDocuments(state): DocNode[] {
      const docs = state.manifest?.documents ?? [];
      const scoped = state.activeRoot === "all" ? docs : docs.filter((doc) => doc.root === state.activeRoot);
      const query = state.query.trim();
      if (!query) return scoped;
      return scoped
        .map((doc) => ({ doc, score: scoreDocument(doc, query) }))
        .filter((item) => item.score > 0)
        .sort((a, b) => b.score - a.score || b.doc.wordCount - a.doc.wordCount)
        .map((item) => item.doc);
    },
    rootDocCounts(state): Record<string, number> {
      return Object.fromEntries((state.manifest?.roots ?? []).map((root) => [root.id, root.docCount]));
    }
  },

  actions: {
    async loadManifest() {
      this.loading = true;
      this.error = "";
      try {
        const response = await fetch(`/content/manifest.json?ts=${Date.now()}`);
        if (!response.ok) throw new Error(`manifest ${response.status}`);
        this.manifest = (await response.json()) as ContentManifest;
        if (!this.currentDocument && this.manifest.documents.length > 0) {
          await this.openDocument(this.manifest.documents[0]);
        }
      } catch (error) {
        this.error = error instanceof Error ? error.message : "内容索引加载失败";
      } finally {
        this.loading = false;
      }
    },

    async openDocument(doc: DocNode) {
      this.documentLoading = true;
      this.error = "";
      try {
        const response = await fetch(`${doc.contentUrl}?ts=${Date.now()}`);
        if (!response.ok) throw new Error(`document ${response.status}`);
        this.currentDocument = (await response.json()) as BlogDocument;
      } catch (error) {
        this.error = error instanceof Error ? error.message : "文档加载失败";
      } finally {
        this.documentLoading = false;
      }
    },

    setRoot(root: string) {
      this.activeRoot = root;
    },

    setQuery(value: string) {
      this.query = value;
    }
  }
});
