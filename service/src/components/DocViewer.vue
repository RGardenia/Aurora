<script setup lang="ts">
import { computed } from "vue";
import { CalendarClock, Hash, LoaderCircle } from "@lucide/vue";
import type { BlogDocument } from "@/types/content";
import { renderMarkdown } from "@/utils/markdown";

const props = defineProps<{
  document: BlogDocument | null;
  loading: boolean;
}>();

const html = computed(() => (props.document ? renderMarkdown(props.document.content) : ""));
const updatedAt = computed(() =>
  props.document
    ? new Intl.DateTimeFormat("zh-CN", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit"
      }).format(new Date(props.document.updatedAt))
    : ""
);
</script>

<template>
  <main class="reader">
    <div v-if="loading" class="loadingState">
      <LoaderCircle :size="28" />
      <span>加载中</span>
    </div>

    <article v-else-if="document" class="article">
      <header class="articleHeader">
        <div class="crumbs">{{ document.root }} / {{ document.segments.join(" / ") }}</div>
        <h1>{{ document.title }}</h1>
        <div class="articleFacts">
          <span><CalendarClock :size="16" />{{ updatedAt }}</span>
          <span><Hash :size="16" />{{ document.wordCount }}</span>
        </div>
      </header>
      <div class="markdownBody" v-html="html"></div>
    </article>

    <div v-else class="emptyState large">暂无文档</div>
  </main>
</template>
