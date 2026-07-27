<script setup lang="ts">
import { FileText } from "@lucide/vue";
import type { DocNode } from "@/types/content";

defineProps<{
  documents: DocNode[];
  currentId?: string;
}>();

const emit = defineEmits<{
  "select-doc": [value: DocNode];
}>();
</script>

<template>
  <section class="docList" aria-label="文章">
    <button
      v-for="doc in documents"
      :key="doc.id"
      :class="{ active: currentId === doc.id }"
      @click="emit('select-doc', doc)"
    >
      <FileText :size="17" />
      <span class="docMeta">
        <strong>{{ doc.title }}</strong>
        <small>{{ doc.path }}</small>
      </span>
      <span class="docStats">{{ doc.wordCount }}</span>
    </button>
    <div v-if="documents.length === 0" class="emptyState">没有匹配文档</div>
  </section>
</template>
