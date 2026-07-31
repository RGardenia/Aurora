import { useState, useMemo, useCallback, useEffect } from 'react'
import {
  ChevronRight, ChevronDown, Folder, FolderOpen, FileText, Search, X, ArrowLeft
} from 'lucide-react'

const ROOT_ACCENT = {
  Algorithm:       '#3eede5',
  BigData:         '#8bede5',
  CloudNative:     '#92bbf3',
  Computer:        '#67c23a',
  DataBase:        '#e6a23c',
  DesultoryEssay:  '#f56c6c',
  Interview:       '#3f86e8',
  Languages:       '#b68cff',
  MachineLearning: '#ff9ec7',
  Middleware:      '#57d68d',
  SuperComputing:  '#c9f268',
  WEB:             '#44b5ff'
}

const ROOT_LABELS = {
  Algorithm: '算法', BigData: '大数据', CloudNative: '云原生', Computer: '计算机基础',
  DataBase: '数据库', DesultoryEssay: '杂文', Interview: '面试', Languages: '编程语言',
  MachineLearning: '机器学习', Middleware: '中间件', SuperComputing: '超算', WEB: 'Web'
}

function TreeNode({ node, level, activeDocId, onSelectDoc, expandedSet, toggleExpand, searchActive }) {
  const isExpanded = expandedSet.has(node.path)
  const isActive = node.type === 'doc' && node.doc?.id === activeDocId
  // When in category view, tree only has one root, so level 0 is that root
  const isRootLevel = level === 0
  const accent = isRootLevel ? ROOT_ACCENT[node.name] || '#3eede5' : null
  const pad = isRootLevel ? 'px-3' : `pl-${Math.min(level * 3 + 3, 24)} pr-3`

  if (node.type === 'doc') {
    return (
      <button
        onClick={() => onSelectDoc(node.doc)}
        className={`w-full text-left flex items-center gap-2 py-1.5 ${pad} text-sm transition-colors rounded-md
          ${isActive
            ? 'dark:bg-aurora-accent/15 dark:text-aurora-accent light:bg-light-accent/15 light:text-light-accent font-medium'
            : 'dark:text-aurora-text dark:hover:bg-aurora-card light:text-light-text light:hover:bg-light-card'
          }`}
      >
        <FileText size={14} className="shrink-0 opacity-50" />
        <span className="truncate">{node.name}</span>
      </button>
    )
  }

  return (
    <div>
      <button
        onClick={() => toggleExpand(node.path)}
        className={`w-full text-left flex items-center gap-1.5 py-1.5 ${pad} text-sm transition-colors rounded-md
          dark:text-aurora-heading dark:hover:bg-aurora-card
          light:text-light-heading light:hover:bg-light-card
          ${isRootLevel ? 'font-bold' : 'font-medium'}`}
      >
        {isRootLevel ? (
          <span className="w-2 h-2 rounded-full shrink-0" style={{ background: accent }} />
        ) : isExpanded ? (
          <ChevronDown size={14} className="shrink-0 opacity-60" />
        ) : (
          <ChevronRight size={14} className="shrink-0 opacity-60" />
        )}
        {level > 0 && (isExpanded
          ? <FolderOpen size={14} className="shrink-0 opacity-40" />
          : <Folder size={14} className="shrink-0 opacity-40" />
        )}
        <span className="truncate" style={isRootLevel ? { color: accent } : undefined}>
          {node.name}
        </span>
        <span className="ml-auto text-xs dark:text-aurora-muted light:text-light-muted shrink-0">
          {node.docCount}
        </span>
      </button>
      {isExpanded && node.children && (
        <div className="tree-children">
          {node.children.map(child => (
            <TreeNode
              key={child.path}
              node={child}
              level={level + 1}
              activeDocId={activeDocId}
              onSelectDoc={onSelectDoc}
              expandedSet={expandedSet}
              toggleExpand={toggleExpand}
              searchActive={searchActive}
            />
          ))}
        </div>
      )}
    </div>
  )
}

function FlatItem({ doc, activeDocId, onSelectDoc }) {
  const accent = ROOT_ACCENT[doc.root] || '#3eede5'
  const isActive = doc.id === activeDocId
  return (
    <button
      onClick={() => onSelectDoc(doc)}
      className={`w-full text-left flex items-center gap-2 px-3 py-1.5 text-sm transition-colors rounded-md
        ${isActive
          ? 'dark:bg-aurora-accent/15 dark:text-aurora-accent light:bg-light-accent/15 light:text-light-accent font-medium'
          : 'dark:text-aurora-text dark:hover:bg-aurora-card light:text-light-text light:hover:bg-light-card'
        }`}
    >
      <span className="w-1.5 h-1.5 rounded-full shrink-0" style={{ background: accent }} />
      <span className="truncate">{doc.title}</span>
      <span className="ml-auto text-xs dark:text-aurora-muted light:text-light-muted shrink-0 truncate max-w-[80px]">
        {doc.segments[doc.segments.length - 1] || doc.root}
      </span>
    </button>
  )
}

export default function TreeSidebar({
  tree, documents, activeDocId, onSelectDoc, loading, autoExpandPath,
  focusRoot, onBackHome
}) {
  const [query, setQuery] = useState('')
  const [expandedSet, setExpandedSet] = useState(new Set())

  // Auto-expand path to active doc
  useEffect(() => {
    if (autoExpandPath && !query) {
      setExpandedSet(prev => {
        const next = new Set(prev)
        for (const node of autoExpandPath) {
          if (node.type !== 'doc') next.add(node.path)
        }
        return next
      })
    }
  }, [autoExpandPath, query])

  const toggleExpand = useCallback((path) => {
    setExpandedSet(prev => {
      const next = new Set(prev)
      if (next.has(path)) next.delete(path)
      else next.add(path)
      return next
    })
  }, [])

  // Search filtering
  const searchResults = useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) return null
    return documents
      .filter(d => {
        const hay = `${d.title} ${d.path} ${d.summary}`.toLowerCase()
        return q.split(/\s+/).every(t => hay.includes(t))
      })
      .slice(0, 200)
  }, [query, documents])

  // Auto-expand root when in category mode
  useEffect(() => {
    if (focusRoot && tree.length > 0) {
      setExpandedSet(new Set(tree.map(r => r.path)))
    } else if (tree.length > 0 && expandedSet.size === 0 && !query) {
      setExpandedSet(new Set(tree.map(r => r.path)))
    }
  }, [tree, focusRoot])

  const accent = focusRoot ? ROOT_ACCENT[focusRoot] || '#3eede5' : '#3eede5'
  const rootLabel = focusRoot ? ROOT_LABELS[focusRoot] || focusRoot : ''

  return (
    <div className="flex flex-col h-full">
      {/* Back to home button (category/reader mode) */}
      {focusRoot && (
        <button
          onClick={onBackHome}
          className="flex items-center gap-2 px-3 py-2.5 text-sm font-medium border-b transition-colors
            dark:hover:bg-aurora-card dark:text-aurora-muted dark:border-aurora-border
            light:hover:bg-light-card light:text-light-muted light:border-light-border"
        >
          <ArrowLeft size={16} />
          <span>返回首页</span>
        </button>
      )}

      {/* Search */}
      <div className="p-3 shrink-0 border-b dark:border-aurora-border light:border-light-border">
        <div className="relative">
          <Search size={16} className="absolute left-3 top-1/2 -translate-y-1/2 dark:text-aurora-muted light:text-light-muted" />
          <input
            value={query}
            onChange={e => setQuery(e.target.value)}
            placeholder="搜索文章…"
            className="w-full pl-9 pr-8 py-2 text-sm rounded-lg outline-none transition-colors
              dark:bg-aurora-card dark:text-aurora-heading dark:placeholder-aurora-muted dark:border-aurora-border
              light:bg-light-card light:text-light-heading light:placeholder-light-muted light:border-light-border
              border focus:ring-1 dark:focus:ring-aurora-accent light:focus:ring-light-accent"
          />
          {query && (
            <button
              onClick={() => setQuery('')}
              className="absolute right-2.5 top-1/2 -translate-y-1/2 dark:text-aurora-muted light:text-light-muted"
            >
              <X size={15} />
            </button>
          )}
        </div>
      </div>

      {/* Tree or search results */}
      <div className="flex-1 overflow-y-auto scroll-thin py-2 min-h-0">
        {loading ? (
          <div className="px-3 py-8 text-center text-sm dark:text-aurora-muted light:text-light-muted">
            正在加载索引…
          </div>
        ) : query && searchResults ? (
          <div>
            <div className="px-3 py-1 text-xs dark:text-aurora-muted light:text-light-muted">
              {searchResults.length} 篇匹配
            </div>
            {searchResults.length === 0 ? (
              <div className="px-3 py-4 text-sm dark:text-aurora-muted light:text-light-muted">
                未找到匹配文章
              </div>
            ) : (
              searchResults.map(doc => (
                <FlatItem key={doc.id} doc={doc} activeDocId={activeDocId} onSelectDoc={onSelectDoc} />
              ))
            )}
          </div>
        ) : (
          tree.map(rootNode => (
            <TreeNode
              key={rootNode.path}
              node={rootNode}
              level={0}
              activeDocId={activeDocId}
              onSelectDoc={onSelectDoc}
              expandedSet={expandedSet}
              toggleExpand={toggleExpand}
              searchActive={!!query}
            />
          ))
        )}
      </div>
    </div>
  )
}
