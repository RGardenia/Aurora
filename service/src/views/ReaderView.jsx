import { useMemo, useEffect, useRef } from 'react'
import { ArrowLeft, CalendarClock, Eye, Hash, ChevronLeft, ChevronRight } from 'lucide-react'
import { renderMarkdown, buildHeadingAnchors } from '../utils/markdown'

const ROOT_ACCENT = {
  Algorithm: '#3eede5', BigData: '#8bede5', CloudNative: '#92bbf3',
  Computer: '#67c23a', DataBase: '#e6a23c', DesultoryEssay: '#f56c6c',
  Interview: '#3f86e8', Languages: '#b68cff', MachineLearning: '#ff9ec7',
  Middleware: '#57d68d', SuperComputing: '#c9f268', WEB: '#44b5ff'
}

function formatDate(value) {
  if (!value) return '未知'
  return new Intl.DateTimeFormat('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(new Date(value))
}

export default function ReaderView({ doc, onBack, onPrev, onNext, prevDoc, nextDoc, loading }) {
  const articleRef = useRef(null)
  const html = useMemo(() => doc ? renderMarkdown(doc.content) : '', [doc])
  const outline = useMemo(() => doc ? buildHeadingAnchors(doc.headings) : [], [doc])
  const accent = doc ? ROOT_ACCENT[doc.root] || '#3eede5' : '#3eede5'

  // ── Copy button handler ──
  useEffect(() => {
    const root = articleRef.current
    if (!root) return
    const handler = (e) => {
      const btn = e.target.closest('.copy-btn')
      if (!btn) return
      const code = btn.getAttribute('data-code')
      if (!code) return
      const unescaped = code
        .replace(/&quot;/g, '"').replace(/&amp;/g, '&')
        .replace(/&lt;/g, '<').replace(/&gt;/g, '>')
      const markCopied = () => {
        btn.textContent = '已复制'
        btn.classList.add('copied')
        setTimeout(() => {
          btn.textContent = '复制代码'
          btn.classList.remove('copied')
        }, 2000)
      }
      // Fallback for non-secure context (HTTP)
      const fallbackCopy = () => {
        const ta = document.createElement('textarea')
        ta.value = unescaped
        ta.style.position = 'fixed'
        ta.style.opacity = '0'
        document.body.appendChild(ta)
        ta.select()
        try { document.execCommand('copy') } catch {}
        document.body.removeChild(ta)
        markCopied()
      }
      try {
        if (navigator.clipboard?.writeText) {
          navigator.clipboard.writeText(unescaped).then(markCopied).catch(fallbackCopy)
        } else {
          fallbackCopy()
        }
      } catch {
        fallbackCopy()
      }
    }
    root.addEventListener('click', handler)
    return () => root.removeEventListener('click', handler)
  }, [html])

  if (loading && !doc) {
    return (
      <div className="flex items-center justify-center h-full">
        <div className="text-sm dark:text-aurora-muted light:text-light-muted">正在加载文档…</div>
      </div>
    )
  }

  if (!doc) return null

  return (
    <div className="flex gap-8 max-w-6xl mx-auto px-4 lg:px-8 py-6">
      {/* Main article */}
      <article ref={articleRef} className="flex-1 min-w-0">
        <button
          onClick={onBack}
          className="inline-flex items-center gap-1.5 mb-4 text-sm transition-colors
            dark:text-aurora-muted dark:hover:text-aurora-accent
            light:text-light-muted light:hover:text-light-accent"
        >
          <ArrowLeft size={16} /> 返回列表
        </button>

        <header className="mb-6 pb-4 dark:border-aurora-border light:border-light-border border-b">
          <div className="flex items-center gap-2 text-xs mb-2" style={{ color: accent }}>
            <span className="font-semibold">{doc.root}</span>
            <span className="opacity-50">/</span>
            <span>{doc.segments.join(' / ')}</span>
          </div>
          <h1 className="text-2xl font-bold mb-3 dark:text-aurora-heading light:text-light-heading">
            {doc.title}
          </h1>
          <div className="flex flex-wrap gap-4 text-xs dark:text-aurora-muted light:text-light-muted">
            <span className="inline-flex items-center gap-1">
              <CalendarClock size={14} /> {formatDate(doc.updatedAt)}
            </span>
            <span className="inline-flex items-center gap-1">
              <Hash size={14} /> {doc.wordCount} 字
            </span>
            <span className="inline-flex items-center gap-1">
              <Eye size={14} /> {Math.max(17, doc.wordCount % 997)}
            </span>
          </div>
        </header>

        <div className="article-body" dangerouslySetInnerHTML={{ __html: html }} />

        {/* Pager */}
        <nav className="mt-12 pt-6 border-t dark:border-aurora-border light:border-light-border grid grid-cols-2 gap-4">
          <button
            disabled={!prevDoc}
            onClick={onPrev}
            className="text-left p-3 rounded-lg transition-colors disabled:opacity-30
              dark:hover:bg-aurora-card light:hover:bg-light-card"
          >
            <span className="flex items-center gap-1 text-xs dark:text-aurora-muted light:text-light-muted">
              <ChevronLeft size={14} /> 上一篇
            </span>
            <strong className="text-sm dark:text-aurora-heading light:text-light-heading truncate block">
              {prevDoc?.title || '没有了'}
            </strong>
          </button>
          <button
            disabled={!nextDoc}
            onClick={onNext}
            className="text-right p-3 rounded-lg transition-colors disabled:opacity-30
              dark:hover:bg-aurora-card light:hover:bg-light-card"
          >
            <span className="flex items-center justify-end gap-1 text-xs dark:text-aurora-muted light:text-light-muted">
              下一篇 <ChevronRight size={14} />
            </span>
            <strong className="text-sm dark:text-aurora-heading light:text-light-heading truncate block">
              {nextDoc?.title || '没有了'}
            </strong>
          </button>
        </nav>
      </article>

      {/* Outline */}
      {outline.length > 0 && (
        <aside className="hidden xl:block w-56 shrink-0">
          <div className="sticky top-6">
            <div className="text-xs font-semibold mb-3 dark:text-aurora-muted light:text-light-muted uppercase tracking-wider">
              目录
            </div>
            <nav className="space-y-1">
              {outline.map(h => (
                <a
                  key={h.id}
                  href={`#${h.id}`}
                  className="block text-xs py-1 px-2 rounded truncate transition-colors
                    dark:text-aurora-text dark:hover:text-aurora-accent dark:hover:bg-aurora-card
                    light:text-light-text light:hover:text-light-accent light:hover:bg-light-card"
                >
                  {h.title}
                </a>
              ))}
            </nav>
          </div>
        </aside>
      )}
    </div>
  )
}
