import { useMemo } from 'react'
import { BookOpen, FileText, Hash, Layers, CalendarClock, ArrowRight, Sparkles } from 'lucide-react'

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

const ROOT_DESC = {
  Algorithm: '剑指Offer、数据结构、动态规划、贪心算法、LeetCode 题解',
  BigData: 'Hadoop、Spark、Flink、Hive、Scala 大数据处理框架',
  CloudNative: 'Docker、Kubernetes、Istio、Jenkins、ELK、DevOps 全栈',
  Computer: '操作系统、计算机网络、Linux、WSL 计算机基础知识',
  DataBase: 'MySQL、Redis、MongoDB、PostgreSQL、GreenPlum、InfluxDB',
  DesultoryEssay: '杂文笔记、Caffine 缓存、Linux 磁盘、办公效率',
  Interview: '大厂面试、基础面试题、面经汇总、面试扫盲学习',
  Languages: 'Java、Golang、Python、JavaScript、C/C++、CUDA、仓颉',
  MachineLearning: '集成学习、深度学习、环境配置、图形学、实用技巧',
  Middleware: 'Kafka、RabbitMQ、Nginx、MyCat、Zookeeper 消息中间件',
  SuperComputing: 'HPC 高性能计算、超级计算笔记',
  WEB: 'Spring、Vue、CSS、HTML、Webpack、Django、JWT Web 开发'
}

function formatDate(value) {
  if (!value) return '未知'
  return new Intl.DateTimeFormat('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(new Date(value))
}

export default function HomeView({ documents, roots, generatedAt, onSelectRoot }) {
  const totalWords = useMemo(() => documents.reduce((s, d) => s + d.wordCount, 0), [documents])
  const recentDocs = useMemo(() =>
    [...documents].sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt)).slice(0, 6),
    [documents]
  )
  const topRoots = useMemo(() =>
    [...roots].sort((a, b) => b.docCount - a.docCount),
    [roots]
  )

  return (
    <div className="min-h-full">
      {/* ── Hero Section ── */}
      <section className="relative overflow-hidden">
        {/* Gradient background */}
        <div className="absolute inset-0 dark:opacity-30 light:opacity-10"
          style={{ background: 'radial-gradient(ellipse at top, #3eede5 0%, transparent 50%), radial-gradient(ellipse at bottom right, #b68cff 0%, transparent 50%)' }} />
        <div className="relative max-w-4xl mx-auto px-4 lg:px-8 pt-16 pb-12 text-center">
          <div className="inline-flex items-center gap-2 text-xs font-medium px-3 py-1 rounded-full mb-5
            dark:bg-aurora-card/60 dark:text-aurora-accent dark:border-aurora-border
            light:bg-light-card/60 light:text-light-accent light:border-light-border
            border backdrop-blur-sm">
            <Sparkles size={14} /> 个人技术知识库
          </div>
          <h1 className="text-4xl lg:text-5xl font-extrabold mb-4 tracking-tight
            dark:text-aurora-heading light:text-light-heading">
            Aurora <span style={{ background: 'linear-gradient(135deg, #3eede5, #b68cff)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>Knowledge</span>
          </h1>
          <p className="text-base lg:text-lg dark:text-aurora-muted light:text-light-muted max-w-2xl mx-auto leading-relaxed">
            {documents.length} 篇技术文档 · {roots.length} 个知识领域 · {totalWords.toLocaleString()} 字
            <br className="hidden sm:inline" />
            <span className="text-xs ml-1">索引生成于 {formatDate(generatedAt)}</span>
          </p>
        </div>
      </section>

      {/* ── Category Cards ── */}
      <section className="max-w-5xl mx-auto px-4 lg:px-8 pb-16">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold dark:text-aurora-heading light:text-light-heading">
            选择知识领域
          </h2>
          <span className="text-xs dark:text-aurora-muted light:text-light-muted">{topRoots.length} 个分类</span>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {topRoots.map(root => {
            const accent = ROOT_ACCENT[root.id] || '#3eede5'
            const label = ROOT_LABELS[root.id] || root.name
            const icon = ROOT_ICONS[root.id] || '📚'
            const desc = ROOT_DESC[root.id] || ''
            return (
              <button
                key={root.id}
                onClick={() => onSelectRoot(root.id)}
                className="group text-left rounded-2xl p-6 border transition-all duration-300
                  hover:scale-[1.03] hover:shadow-xl
                  dark:bg-aurora-card dark:border-aurora-border dark:hover:border-transparent
                  light:bg-light-card light:border-light-border light:hover:border-transparent
                  relative overflow-hidden"
                style={{ '--accent': accent }}
              >
                {/* Accent glow on hover */}
                <div className="absolute inset-0 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
                  style={{ background: `radial-gradient(circle at top left, ${accent}15 0%, transparent 60%)` }} />

                <div className="relative">
                  <div className="flex items-start justify-between mb-4">
                    <div className="text-3xl">{icon}</div>
                    <div className="w-10 h-10 rounded-full flex items-center justify-center text-xs font-bold"
                      style={{ background: accent + '20', color: accent }}>
                      {root.docCount}
                    </div>
                  </div>
                  <h3 className="text-lg font-bold mb-1 dark:text-aurora-heading light:text-light-heading">
                    {label}
                  </h3>
                  <p className="text-xs dark:text-aurora-muted light:text-light-muted leading-relaxed line-clamp-2">
                    {desc}
                  </p>
                  <div className="flex items-center gap-1 mt-4 text-xs font-medium transition-all group-hover:gap-2"
                    style={{ color: accent }}>
                    进入浏览 <ArrowRight size={14} />
                  </div>
                </div>
              </button>
            )
          })}
        </div>
      </section>

      {/* ── Recent Updates ── */}
      <section className="max-w-5xl mx-auto px-4 lg:px-8 pb-20">
        <div className="flex items-center gap-2 mb-6">
          <div className="w-1 h-5 rounded-full bg-gradient-to-b from-aurora-accent to-aurora-link" />
          <h2 className="text-xl font-bold dark:text-aurora-heading light:text-light-heading">
            最近更新
          </h2>
        </div>
        <div className="grid gap-2">
          {recentDocs.map(doc => {
            const accent = ROOT_ACCENT[doc.root] || '#3eede5'
            const label = ROOT_LABELS[doc.root] || doc.root
            return (
              <button
                key={doc.id}
                onClick={() => onSelectRoot(doc.root)}
                className="group text-left flex items-center gap-4 p-4 rounded-xl transition-all duration-200
                  dark:hover:bg-aurora-card light:hover:bg-light-card
                  border border-transparent hover:border-current/10"
              >
                <div className="w-1 h-10 rounded-full shrink-0" style={{ background: accent }} />
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-1">
                    <span className="text-sm font-semibold dark:text-aurora-heading light:text-light-heading truncate group-hover:text-aurora-accent transition-colors">
                      {doc.title}
                    </span>
                    <span className="text-[10px] px-1.5 py-0.5 rounded-full shrink-0 font-medium"
                      style={{ background: accent + '20', color: accent }}>
                      {label}
                    </span>
                  </div>
                  <p className="text-xs dark:text-aurora-muted light:text-light-muted truncate">
                    {doc.summary || doc.path}
                  </p>
                </div>
                <span className="text-xs dark:text-aurora-muted light:text-light-muted shrink-0 hidden sm:inline whitespace-nowrap">
                  {formatDate(doc.updatedAt)}
                </span>
                <ArrowRight size={16} className="opacity-0 group-hover:opacity-40 transition-opacity shrink-0" />
              </button>
            )
          })}
        </div>
      </section>

      {/* ── Footer stats ── */}
      <footer className="max-w-5xl mx-auto px-4 lg:px-8 pb-8">
        <div className="grid grid-cols-3 gap-3">
          {[
            { icon: FileText, label: '文档总数', value: documents.length, color: '#3eede5' },
            { icon: Layers, label: '知识领域', value: roots.length, color: '#92bbf3' },
            { icon: Hash, label: '总字数', value: totalWords > 10000 ? `${(totalWords / 10000).toFixed(1)}万` : totalWords.toLocaleString(), color: '#ff9ec7' }
          ].map((s, i) => (
            <div key={i} className="rounded-xl p-4 border text-center
              dark:bg-aurora-card dark:border-aurora-border
              light:bg-light-card light:border-light-border">
              <div className="flex justify-center mb-2">
                <s.icon size={18} style={{ color: s.color }} />
              </div>
              <div className="text-lg font-bold dark:text-aurora-heading light:text-light-heading">{s.value}</div>
              <div className="text-[10px] dark:text-aurora-muted light:text-light-muted">{s.label}</div>
            </div>
          ))}
        </div>
      </footer>
    </div>
  )
}
