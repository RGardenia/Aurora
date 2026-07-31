import { useMemo, useState } from 'react'
import { ArrowLeft, ArrowRight, Search, FileText, ChevronRight } from 'lucide-react'

const ROOT_ACCENT = {
  Algorithm: '#3eede5', BigData: '#8bede5', CloudNative: '#92bbf3',
  Computer: '#67c23a', DataBase: '#e6a23c', DesultoryEssay: '#f56c6c',
  Interview: '#3f86e8', Languages: '#b68cff', MachineLearning: '#ff9ec7',
  Middleware: '#57d68d', SuperComputing: '#c9f268', WEB: '#44b5ff'
}

const ROOT_LABELS = {
  Algorithm: '算法', BigData: '大数据', CloudNative: '云原生', Computer: '计算机基础',
  DataBase: '数据库', DesultoryEssay: '杂文', Interview: '面试', Languages: '编程语言',
  MachineLearning: '机器学习', Middleware: '中间件', SuperComputing: '超算', WEB: 'Web'
}

const ROOT_ICONS = {
  Algorithm: '🧮', BigData: '📊', CloudNative: '☁️', Computer: '💻',
  DataBase: '🗄️', DesultoryEssay: '📝', Interview: '🎯', Languages: '⌨️',
  MachineLearning: '🤖', Middleware: '🔌', SuperComputing: '⚡', WEB: '🌐'
}

function formatDate(value) {
  if (!value) return ''
  return new Intl.DateTimeFormat('zh-CN', { month: '2-digit', day: '2-digit' }).format(new Date(value))
}

export default function CategoryView({
  rootId, rootName, documents, onSelectDoc, onBack
}) {
  const [query, setQuery] = useState('')
  const [sortBy, setSortBy] = useState('update')  // 'update' | 'title'

  const accent = ROOT_ACCENT[rootId] || '#3eede5'
  const label = ROOT_LABELS[rootId] || rootName
  const icon = ROOT_ICONS[rootId] || '📚'

  const filteredDocs = useMemo(() => {
    let docs = documents.filter(d => d.root === rootId)
    if (query.trim()) {
      const q = query.trim().toLowerCase()
      docs = docs.filter(d => {
        const hay = `${d.title} ${d.path} ${d.summary || ''}`.toLowerCase()
        return q.split(/\s+/).every(t => hay.includes(t))
      })
    }
    if (sortBy === 'title') {
      docs = [...docs].sort((a, b) => a.title.localeCompare(b.title, 'zh-Hans-CN'))
    } else {
      docs = [...docs].sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt))
    }
    return docs
  }, [documents, rootId, query, sortBy])

  // Group by first segment (subdirectory)
  const grouped = useMemo(() => {
    const groups = new Map()
    for (const doc of filteredDocs) {
      const subDir = doc.segments[0] || '根目录'
      if (!groups.has(subDir)) groups.set(subDir, [])
      groups.get(subDir).push(doc)
    }
    return Array.from(groups.entries()).sort((a, b) => a[0].localeCompare(b[0], 'zh-Hans-CN'))
  }, [filteredDocs])

  return (
    <div className="min-h-full">
      {/* ── Category Header ── */}
      <div className="relative overflow-hidden border-b dark:border-aurora-border light:border-light-border">
        <div className="absolute inset-0 opacity-20"
          style={{ background: `linear-gradient(135deg, ${accent}30 0%, transparent 100%)` }} />
        <div className="relative max-w-4xl mx-auto px-4 lg:px-8 py-8">
          <button
            onClick={onBack}
            className="flex items-center gap-1.5 text-xs dark:text-aurora-muted light:text-light-muted hover:opacity-80 transition-opacity mb-4"
          >
            <ArrowLeft size={14} /> 返回首页
          </button>
          <div className="flex items-center gap-4">
            <div className="text-4xl lg:text-5xl">{icon}</div>
            <div>
              <h1 className="text-2xl lg:text-3xl font-extrabold dark:text-aurora-heading light:text-light-heading">
                {label}
              </h1>
              <p className="text-sm mt-1" style={{ color: accent }}>
                {rootName} · {filteredDocs.length} 篇文档
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* ── Toolbar ── */}
      <div className="sticky top-0 z-10 backdrop-blur-md
        dark:bg-aurora-bg/80 light:bg-light-bg/80
        border-b dark:border-aurora-border light:border-light-border">
        <div className="max-w-4xl mx-auto px-4 lg:px-8 py-3 flex items-center gap-3">
          <div className="relative flex-1">
            <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 dark:text-aurora-muted light:text-light-muted" />
            <input
              value={query}
              onChange={e => setQuery(e.target.value)}
              placeholder="搜索本分类文章…"
              className="w-full pl-9 pr-3 py-2 text-sm rounded-lg outline-none transition-colors
                dark:bg-aurora-card dark:text-aurora-heading dark:placeholder-aurora-muted dark:border-aurora-border
                light:bg-light-card light:text-light-heading light:placeholder-light-muted light:border-light-border
                border focus:ring-1 dark:focus:ring-aurora-accent light:focus:ring-light-accent"
            />
          </div>
          <div className="flex items-center gap-1 rounded-lg p-0.5
            dark:bg-aurora-card dark:border-aurora-border
            light:bg-light-card light:border-light-border border">
            <button
              onClick={() => setSortBy('update')}
              className={`px-2.5 py-1 text-xs rounded-md transition-colors
                ${sortBy === 'update'
                  ? 'dark:bg-aurora-accent/20 dark:text-aurora-accent light:bg-light-accent/20 light:text-light-accent'
                  : 'dark:text-aurora-muted light:text-light-muted'}`}
            >最新</button>
            <button
              onClick={() => setSortBy('title')}
              className={`px-2.5 py-1 text-xs rounded-md transition-colors
                ${sortBy === 'title'
                  ? 'dark:bg-aurora-accent/20 dark:text-aurora-accent light:bg-light-accent/20 light:text-light-accent'
                  : 'dark:text-aurora-muted light:text-light-muted'}`}
            >字母</button>
          </div>
        </div>
      </div>

      {/* ── Document List ── */}
      <div className="max-w-4xl mx-auto px-4 lg:px-8 py-6">
        {filteredDocs.length === 0 ? (
          <div className="text-center py-20 dark:text-aurora-muted light:text-light-muted">
            <FileText size={48} className="mx-auto mb-4 opacity-20" />
            <p>未找到匹配文章</p>
          </div>
        ) : (
          <div className="space-y-8">
            {grouped.map(([subDir, docs]) => (
              <div key={subDir}>
                <h3 className="text-xs font-bold uppercase tracking-wider mb-3 flex items-center gap-2"
                  style={{ color: accent }}>
                  <ChevronRight size={14} />
                  {subDir}
                  <span className="dark:text-aurora-muted light:text-light-muted font-normal normal-case tracking-normal">
                    · {docs.length} 篇
                  </span>
                </h3>
                <div className="grid gap-1.5">
                  {docs.map(doc => (
                    <button
                      key={doc.id}
                      onClick={() => onSelectDoc(doc)}
                      className="group text-left flex items-center gap-3 p-3 rounded-lg transition-all duration-200
                        dark:hover:bg-aurora-card light:hover:bg-light-card
                        border border-transparent dark:hover:border-aurora-border light:hover:border-light-border"
                    >
                      <div className="w-1 h-8 rounded-full shrink-0 opacity-30 group-hover:opacity-100 transition-opacity"
                        style={{ background: accent }} />
                      <div className="flex-1 min-w-0">
                        <div className="text-sm font-medium dark:text-aurora-heading light:text-light-heading
                          group-hover:text-aurora-accent light:group-hover:text-light-accent transition-colors truncate">
                          {doc.title}
                        </div>
                        <div className="text-xs dark:text-aurora-muted light:text-light-muted truncate mt-0.5">
                          {doc.summary || doc.path}
                        </div>
                      </div>
                      <span className="text-xs dark:text-aurora-muted light:text-light-muted shrink-0 whitespace-nowrap">
                        {formatDate(doc.updatedAt)}
                      </span>
                      <ArrowRight size={14} className="opacity-0 group-hover:opacity-40 transition-opacity shrink-0" />
                    </button>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
