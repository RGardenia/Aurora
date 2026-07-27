<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import {
  ArrowDown,
  ArrowLeft,
  BookOpen,
  CalendarClock,
  Code2,
  Eye,
  Hash,
  LoaderCircle,
  Menu,
  RefreshCw,
  Search,
  Tag,
  X
} from "@lucide/vue";
import { useLibraryStore } from "@/stores/library";
import type { DocNode } from "@/types/content";
import { buildHeadingAnchors, renderMarkdown } from "@/utils/markdown";

const library = useLibraryStore();
const view = ref<"home" | "article">("home");
const mobileMenuOpen = ref(false);

const heroLayers = [
  "https://media.baby7blog.com/blog/img/banner/ppp0.png-to75.webp",
  "https://media.baby7blog.com/blog/img/banner/pp1.png-to75.webp",
  "https://media.baby7blog.com/blog/img/banner/pp2.png-to75.webp",
  "https://media.baby7blog.com/blog/img/banner/pp3.png-to75.webp",
  "https://media.baby7blog.com/blog/img/banner/pp4.png-to75.webp",
  "https://media.baby7blog.com/blog/img/banner/pp6.png-to75.webp"
];

const rootAccent: Record<string, string> = {
  Algorithm: "#3eede5",
  BigData: "#8bede5",
  CloudNative: "#92bbf3",
  Computer: "#67c23a",
  DataBase: "#e6a23c",
  DesultoryEssay: "#f56c6c",
  Interview: "#3f86e8",
  Languages: "#b68cff",
  MachineLearning: "#ff9ec7",
  Middleware: "#57d68d",
  SuperComputing: "#c9f268",
  WEB: "#44b5ff"
};

const currentId = computed(() => library.currentDocument?.id);
const selectedHtml = computed(() => (library.currentDocument ? renderMarkdown(library.currentDocument.content) : ""));
const outlineLinks = computed(() => buildHeadingAnchors(library.currentDocument?.headings ?? []));
const visibleDocs = computed(() => library.filteredDocuments.slice(0, 80));
const selectedIndex = computed(() => library.documents.findIndex((doc) => doc.id === currentId.value));
const previousDoc = computed(() => (selectedIndex.value > 0 ? library.documents[selectedIndex.value - 1] : null));
const nextDoc = computed(() =>
  selectedIndex.value >= 0 && selectedIndex.value < library.documents.length - 1
    ? library.documents[selectedIndex.value + 1]
    : null
);
const generatedAt = computed(() => formatDate(library.manifest?.generatedAt));
const topRoots = computed(() => [...library.roots].sort((a, b) => b.docCount - a.docCount).slice(0, 8));

function formatDate(value?: string) {
  if (!value) return "未知时间";
  return new Intl.DateTimeFormat("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit"
  }).format(new Date(value));
}

function scrollToContent() {
  document.getElementById("content")?.scrollIntoView({ behavior: "smooth", block: "start" });
}

function selectRoot(root: string) {
  library.setRoot(root);
  view.value = "home";
  mobileMenuOpen.value = false;
  requestAnimationFrame(scrollToContent);
}

async function openArticle(doc: DocNode | null) {
  if (!doc) return;
  await library.openDocument(doc);
  view.value = "article";
  mobileMenuOpen.value = false;
  requestAnimationFrame(scrollToContent);
}

function coverFor(doc: DocNode) {
  const encoded = encodeURIComponent(doc.root);
  return `https://media.baby7blog.com/blog/img/banner/pp${(encoded.length % 4) + 1}.png-to75.webp`;
}

onMounted(() => {
  void library.loadManifest();
});
</script>

<template>
  <div class="blogShell">
    <header class="topNav">
      <button class="iconBtn navMenu" title="菜单" @click="mobileMenuOpen = true">
        <Menu :size="22" />
      </button>
      <button class="navBrand" @click="view = 'home'">
        <BookOpen :size="22" />
        <span>Aurora</span>
      </button>
      <nav>
        <button :class="{ active: view === 'home' }" @click="view = 'home'">首页</button>
        <button @click="selectRoot('Languages')">语言</button>
        <button @click="selectRoot('WEB')">Web</button>
        <button @click="selectRoot('CloudNative')">云原生</button>
      </nav>
      <button class="iconBtn" title="刷新索引" @click="library.loadManifest">
        <RefreshCw :size="19" />
      </button>
    </header>

    <aside class="mobileDrawer" :class="{ show: mobileMenuOpen }">
      <button class="iconBtn drawerClose" title="关闭" @click="mobileMenuOpen = false">
        <X :size="20" />
      </button>
      <strong>知识目录</strong>
      <button :class="{ active: library.activeRoot === 'all' }" @click="selectRoot('all')">全部</button>
      <button v-for="root in library.roots" :key="root.id" :class="{ active: library.activeRoot === root.id }" @click="selectRoot(root.id)">
        {{ root.name }} <span>{{ root.docCount }}</span>
      </button>
    </aside>
    <div class="drawerMask" :class="{ show: mobileMenuOpen }" @click="mobileMenuOpen = false"></div>

    <section class="hero">
      <img v-for="(layer, index) in heroLayers" :key="layer" :src="layer" :class="`heroLayer layer${index}`" alt="" />
      <div class="heroFallback"></div>
      <div class="heroContent">
        <p class="heroKicker">Aurora Knowledge Blog</p>
        <h1>把笔记变成可浏览的知识宇宙</h1>
        <p>映射仓库里的 Markdown 文档，以 Baby7 暗色博客风格重新呈现。</p>
      </div>
      <button class="scrollCue" title="查看文章" @click="scrollToContent">
        <ArrowDown :size="28" />
      </button>
    </section>

    <main id="content" class="mainStage">
      <section v-if="view === 'home'" class="contentGrid">
        <div class="articleColumn">
          <div class="toolbar">
            <label class="searchBox" title="搜索文章">
              <Search :size="18" />
              <input
                :value="library.query"
                placeholder="搜索标题、路径、摘要"
                @input="library.setQuery(($event.target as HTMLInputElement).value)"
              />
            </label>
            <div class="resultCount">{{ library.filteredDocuments.length }} 篇</div>
          </div>

          <div v-if="library.loading" class="loadingBlock">
            <LoaderCircle :size="28" />
            <span>正在生成星图</span>
          </div>

          <article v-for="doc in visibleDocs" v-else :key="doc.id" class="postCard" @click="openArticle(doc)">
            <img :src="coverFor(doc)" :alt="doc.title" loading="lazy" />
            <div>
              <div class="postRoot" :style="{ color: rootAccent[doc.root] ?? '#3eede5' }">{{ doc.root }}</div>
              <h2>{{ doc.title }}</h2>
              <p>{{ doc.summary || doc.path }}</p>
              <footer>
                <span><CalendarClock :size="15" />{{ formatDate(doc.updatedAt) }}</span>
                <span><Hash :size="15" />{{ doc.wordCount }}</span>
                <span><Eye :size="15" />{{ Math.max(17, doc.wordCount % 997) }}</span>
              </footer>
            </div>
          </article>
        </div>

        <aside class="infoRail">
          <section class="profilePanel">
            <img src="https://media.baby7blog.com/theme/back.jpg" alt="Aurora" />
            <h2>Aurora</h2>
            <p>一座由算法、云原生、语言、Web 与工程笔记组成的知识花园。</p>
            <div class="profileStats">
              <span><strong>{{ library.documents.length }}</strong>文章</span>
              <span><strong>{{ library.roots.length }}</strong>目录</span>
            </div>
          </section>

          <section class="sidePanel">
            <h3>知识目录</h3>
            <button :class="{ active: library.activeRoot === 'all' }" @click="selectRoot('all')">全部文档</button>
            <button v-for="root in topRoots" :key="root.id" :class="{ active: library.activeRoot === root.id }" @click="selectRoot(root.id)">
              <span>{{ root.name }}</span>
              <small>{{ root.docCount }}</small>
            </button>
          </section>

          <section class="sidePanel chips">
            <h3>标签</h3>
            <button v-for="root in library.roots" :key="root.id" @click="selectRoot(root.id)">
              <Tag :size="13" />{{ root.name }}
            </button>
          </section>

          <section class="sidePanel">
            <h3>站点</h3>
            <a href="https://github.com/RGardenia/Aurora" target="_blank" rel="noreferrer">
              <Code2 :size="16" />RGardenia/Aurora
            </a>
            <small>索引更新时间：{{ generatedAt }}</small>
          </section>
        </aside>
      </section>

      <section v-else class="readerLayout">
        <article v-if="library.currentDocument" class="readerCard">
          <button class="backLink" @click="view = 'home'">
            <ArrowLeft :size="18" />返回列表
          </button>
          <header class="articleHeader">
            <div class="postRoot" :style="{ color: rootAccent[library.currentDocument.root] ?? '#3eede5' }">
              {{ library.currentDocument.root }} / {{ library.currentDocument.segments.join(" / ") }}
            </div>
            <h1>{{ library.currentDocument.title }}</h1>
            <div class="articleFacts">
              <span><CalendarClock :size="16" />发布时间：{{ formatDate(library.currentDocument.updatedAt) }}</span>
              <span><CalendarClock :size="16" />最后更新：{{ formatDate(library.currentDocument.updatedAt) }}</span>
              <span><Eye :size="16" />浏览量：{{ Math.max(17, library.currentDocument.wordCount % 997) }}</span>
            </div>
          </header>
          <div class="markdownBody" v-html="selectedHtml"></div>
          <nav class="articlePager">
            <button :disabled="!previousDoc" @click="openArticle(previousDoc)">
              <span>上一篇</span>
              <strong>{{ previousDoc?.title ?? "没有了" }}</strong>
            </button>
            <button :disabled="!nextDoc" @click="openArticle(nextDoc)">
              <span>下一篇</span>
              <strong>{{ nextDoc?.title ?? "没有了" }}</strong>
            </button>
          </nav>
        </article>

        <aside class="articleOutline">
          <strong>文章目录</strong>
          <a v-for="heading in outlineLinks" :key="heading.id" :href="`#${heading.id}`">{{ heading.title }}</a>
        </aside>
      </section>
    </main>

    <p v-if="library.error" class="toast">{{ library.error }}</p>
  </div>
</template>
