import { useState, useEffect, useMemo, useCallback } from 'react'
import Topbar from './components/Topbar'
import TreeSidebar from './components/TreeSidebar'
import HomeView from './views/HomeView'
import CategoryView from './views/CategoryView'
import ReaderView from './views/ReaderView'
import { buildTree, findPathToDoc } from './utils/tree'

export default function App() {
  const [manifest, setManifest] = useState(null)
  const [currentDoc, setCurrentDoc] = useState(null)
  // 'home' | 'category' | 'reader'
  const [view, setView] = useState('home')
  const [focusRoot, setFocusRoot] = useState(null)
  const [loading, setLoading] = useState(true)
  const [docLoading, setDocLoading] = useState(false)
  const [error, setError] = useState('')
  const [sidebarOpen, setSidebarOpen] = useState(false)

  // ── Load manifest ──
  const loadManifest = useCallback(async () => {
    setLoading(true)
    setError('')
    try {
      const res = await fetch(`/content/manifest.json?ts=${Date.now()}`)
      if (!res.ok) throw new Error(`manifest ${res.status}`)
      const data = await res.json()
      setManifest(data)
    } catch (e) {
      setError(e instanceof Error ? e.message : '索引加载失败')
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { loadManifest() }, [loadManifest])

  // ── Documents & tree ──
  const documents = useMemo(() => manifest?.documents ?? [], [manifest])
  const roots = useMemo(() => manifest?.roots ?? [], [manifest])
  const tree = useMemo(() => buildTree(documents), [documents])

  // ── Sidebar tree: filter by focusRoot on category/reader views ──
  const sidebarTree = useMemo(() => {
    if (view === 'home' || !focusRoot) return tree
    const filtered = tree.filter(n => n.name === focusRoot)
    return filtered
  }, [tree, view, focusRoot])

  // ── Active doc state ──
  const activeDocId = currentDoc?.id ?? null
  const autoExpandPath = useMemo(() => {
    if (!activeDocId || tree.length === 0) return null
    return findPathToDoc(tree, activeDocId)
  }, [activeDocId, tree])

  // ── Open document ──
  const openDoc = useCallback(async (doc) => {
    if (!doc) return
    setDocLoading(true)
    setError('')
    setView('reader')
    setSidebarOpen(false)
    setFocusRoot(doc.root)
    try {
      if (currentDoc?.id === doc.id && currentDoc.content) {
        setDocLoading(false)
        return
      }
      const res = await fetch(`${doc.contentUrl}?ts=${Date.now()}`)
      if (!res.ok) throw new Error(`document ${res.status}`)
      const full = await res.json()
      setCurrentDoc(full)
    } catch (e) {
      setError(e instanceof Error ? e.message : '文档加载失败')
    } finally {
      setDocLoading(false)
    }
  }, [currentDoc])

  // ── Prev / Next navigation (within same root) ──
  const rootDocs = useMemo(() => {
    if (!focusRoot) return documents
    return documents.filter(d => d.root === focusRoot)
  }, [documents, focusRoot])

  const docIndex = useMemo(() => rootDocs.findIndex(d => d.id === activeDocId), [rootDocs, activeDocId])
  const prevDoc = docIndex > 0 ? rootDocs[docIndex - 1] : null
  const nextDoc = docIndex >= 0 && docIndex < rootDocs.length - 1 ? rootDocs[docIndex + 1] : null

  // ── Select root from home → enter category view ──
  const handleSelectRoot = useCallback((rootId) => {
    setView('category')
    setFocusRoot(rootId)
    setCurrentDoc(null)
    setSidebarOpen(false)
  }, [])

  // ── Back to home ──
  const handleBackHome = useCallback(() => {
    setView('home')
    setFocusRoot(null)
    setCurrentDoc(null)
  }, [])

  // ── Root info for category/reader views ──
  const focusRootInfo = useMemo(() => {
    if (!focusRoot) return null
    return roots.find(r => r.id === focusRoot) || { id: focusRoot, name: focusRoot }
  }, [roots, focusRoot])

  // ── Layout ──
  return (
    <div className="flex h-screen overflow-hidden
      dark:bg-aurora-bg light:bg-light-bg">
      {/* Sidebar — hidden on home view, visible on category/reader views */}
      {view !== 'home' && (
        <aside
          className={`fixed lg:static inset-y-0 left-0 z-40 w-72 shrink-0 border-r flex flex-col
            transition-transform duration-200
            ${sidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
            dark:bg-aurora-surface dark:border-aurora-border
            light:bg-white light:border-light-border`}
        >
          <TreeSidebar
            tree={sidebarTree}
            documents={focusRoot ? documents.filter(d => d.root === focusRoot) : documents}
            activeDocId={activeDocId}
            onSelectDoc={openDoc}
            loading={loading}
            autoExpandPath={autoExpandPath}
            focusRoot={focusRoot}
            onBackHome={handleBackHome}
          />
        </aside>
      )}

      {/* Mobile overlay */}
      {sidebarOpen && view !== 'home' && (
        <div
          className="fixed inset-0 z-30 bg-black/40 lg:hidden"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {/* Main area */}
      <div className="flex flex-col flex-1 min-w-0">
        <Topbar
          onRefresh={loadManifest}
          docCount={documents.length}
          rootCount={roots.length}
          onToggleSidebar={() => setSidebarOpen(!sidebarOpen)}
          showSidebarToggle={view !== 'home'}
        />

        <main className="flex-1 overflow-y-auto scroll-thin
          dark:bg-aurora-bg light:bg-light-bg">
          {error && (
            <div className="px-4 py-2 text-sm text-center text-red-400">
              {error}
            </div>
          )}
          {view === 'home' ? (
            <HomeView
              documents={documents}
              roots={roots}
              generatedAt={manifest?.generatedAt}
              onSelectRoot={handleSelectRoot}
            />
          ) : view === 'category' ? (
            <CategoryView
              rootId={focusRoot}
              rootName={focusRootInfo?.name || focusRoot}
              documents={documents}
              onSelectDoc={openDoc}
              onBack={handleBackHome}
            />
          ) : (
            <ReaderView
              doc={currentDoc}
              onBack={() => { setView('category') }}
              onPrev={() => prevDoc && openDoc(prevDoc)}
              onNext={() => nextDoc && openDoc(nextDoc)}
              prevDoc={prevDoc}
              nextDoc={nextDoc}
              loading={docLoading}
            />
          )}
        </main>
      </div>
    </div>
  )
}
