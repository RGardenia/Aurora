import { useEffect, useState } from 'react'
import { BookOpen, Moon, Sun, RefreshCw, Menu } from 'lucide-react'

export default function Topbar({ onRefresh, docCount, rootCount, onToggleSidebar, showSidebarToggle = true }) {
  const [dark, setDark] = useState(() => {
    const saved = localStorage.getItem('aurora-theme')
    return saved ? saved === 'dark' : true
  })

  useEffect(() => {
    document.documentElement.className = dark ? 'dark' : 'light'
    localStorage.setItem('aurora-theme', dark ? 'dark' : 'light')
  }, [dark])

  return (
    <header className="flex items-center justify-between px-4 lg:px-6 h-14 border-b shrink-0
      dark:bg-aurora-surface dark:border-aurora-border
      light:bg-white light:border-light-border">
      <div className="flex items-center gap-3">
        {showSidebarToggle && (
          <button
            onClick={onToggleSidebar}
            className="lg:hidden p-2 -ml-2 rounded-lg transition-colors dark:text-aurora-muted dark:hover:bg-aurora-card light:text-light-muted light:hover:bg-light-card"
            title="目录"
          >
            <Menu size={20} />
          </button>
        )}
        <div className="flex items-center gap-2.5 font-bold text-base">
          <BookOpen size={22} className="dark:text-aurora-accent light:text-light-accent" />
          <span className="dark:text-aurora-accent light:text-light-accent">Aurora</span>
          <span className="hidden sm:inline text-xs font-normal dark:text-aurora-muted light:text-light-muted ml-2">
            {docCount} 篇 · {rootCount} 目录
          </span>
        </div>
      </div>
      <div className="flex items-center gap-2">
        <button
          onClick={onRefresh}
          className="p-2 rounded-lg transition-colors dark:text-aurora-muted dark:hover:bg-aurora-card light:text-light-muted light:hover:bg-light-card"
          title="刷新索引"
        >
          <RefreshCw size={18} />
        </button>
        <button
          onClick={() => setDark(!dark)}
          className="p-2 rounded-lg transition-colors dark:text-aurora-muted dark:hover:bg-aurora-card light:text-light-muted light:hover:bg-light-card"
          title="切换主题"
        >
          {dark ? <Sun size={18} /> : <Moon size={18} />}
        </button>
      </div>
    </header>
  )
}
