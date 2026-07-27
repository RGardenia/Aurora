<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { Menu, RefreshCw, X } from "@lucide/vue";
import DocViewer from "@/components/DocViewer.vue";
import DocumentList from "@/components/DocumentList.vue";
import Sidebar from "@/components/Sidebar.vue";
import { useLibraryStore } from "@/stores/library";
import { buildHeadingAnchors } from "@/utils/markdown";

const library = useLibraryStore();
const sidebarOpen = ref(false);

const currentId = computed(() => library.currentDocument?.id);
const generatedAt = computed(() =>
  library.manifest
    ? new Intl.DateTimeFormat("zh-CN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit"
      }).format(new Date(library.manifest.generatedAt))
    : ""
);
const outlineLinks = computed(() => buildHeadingAnchors(library.currentDocument?.headings ?? []));

onMounted(() => {
  void library.loadManifest();
});
</script>

<template>
  <div class="appShell">
    <header class="mobileBar">
      <button class="iconButton" title="菜单" @click="sidebarOpen = true">
        <Menu :size="21" />
      </button>
      <strong>Aurora Knowledge</strong>
      <button class="iconButton" title="刷新" @click="library.loadManifest">
        <RefreshCw :size="19" />
      </button>
    </header>

    <div class="layout">
      <div class="sidebarOverlay" :class="{ show: sidebarOpen }" @click="sidebarOpen = false"></div>
      <div class="sidebarPane" :class="{ show: sidebarOpen }">
        <button class="iconButton closeButton" title="关闭" @click="sidebarOpen = false">
          <X :size="20" />
        </button>
        <Sidebar
          :roots="library.roots"
          :documents="library.documents"
          :active-root="library.activeRoot"
          :query="library.query"
          :current-id="currentId"
          @update:query="library.setQuery"
          @select-root="library.setRoot"
          @select-doc="library.openDocument"
        />
      </div>

      <DocumentList
        :documents="library.filteredDocuments"
        :current-id="currentId"
        @select-doc="(doc) => { library.openDocument(doc); sidebarOpen = false; }"
      />

      <DocViewer :document="library.currentDocument" :loading="library.documentLoading || library.loading" />

      <aside class="outline">
        <div class="outlineBlock">
          <span>索引</span>
          <strong>{{ library.documents.length }}</strong>
          <small>{{ generatedAt }}</small>
        </div>
        <nav v-if="outlineLinks.length" aria-label="文章目录">
          <a v-for="heading in outlineLinks" :key="heading.id" :href="`#${heading.id}`">
            {{ heading.title }}
          </a>
        </nav>
      </aside>
    </div>

    <p v-if="library.error" class="toast">{{ library.error }}</p>
  </div>
</template>
