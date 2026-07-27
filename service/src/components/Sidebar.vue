<script setup lang="ts">
import { BookOpen, Folder, Search } from "@lucide/vue";
import type { DocNode, KnowledgeRoot } from "@/types/content";

defineProps<{
  roots: KnowledgeRoot[];
  documents: DocNode[];
  activeRoot: string;
  query: string;
  currentId?: string;
}>();

const emit = defineEmits<{
  "update:query": [value: string];
  "select-root": [value: string];
  "select-doc": [value: DocNode];
}>();
</script>

<template>
  <aside class="sidebar">
    <div class="brand">
      <BookOpen :size="24" />
      <div>
        <strong>Aurora</strong>
        <span>Knowledge</span>
      </div>
    </div>

    <label class="searchBox" title="搜索">
      <Search :size="18" />
      <input :value="query" placeholder="搜索标题、路径、摘要" @input="emit('update:query', ($event.target as HTMLInputElement).value)" />
    </label>

    <nav class="rootList" aria-label="知识目录">
      <button :class="{ active: activeRoot === 'all' }" @click="emit('select-root', 'all')">
        <Folder :size="17" />
        <span>全部</span>
        <small>{{ documents.length }}</small>
      </button>
      <button
        v-for="root in roots"
        :key="root.id"
        :class="{ active: activeRoot === root.id }"
        @click="emit('select-root', root.id)"
      >
        <Folder :size="17" />
        <span>{{ root.name }}</span>
        <small>{{ root.docCount }}</small>
      </button>
    </nav>
  </aside>
</template>
