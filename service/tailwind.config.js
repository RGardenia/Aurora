/** @type {import('tailwindcss').Config} */
const lightVariant = ({ addVariant }) => {
  addVariant('light', ({ modifySelectors, separator }) => {
    modifySelectors(({ className }) => {
      return `.light .${className.replace(/light[\\]/g, '')}`
    })
  })
}

export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        aurora: {
          bg: '#202124', surface: '#28292c', card: '#2d2e31',
          border: '#3a3b3d', muted: '#8b8b8d', text: '#b1b1b1',
          heading: '#eaecef', accent: '#3eede5', link: '#6fb3ff', code: '#1e1e1e'
        },
        light: {
          bg: '#faf9f6', surface: '#ffffff', card: '#f5f3ef',
          border: '#e2ddd5', muted: '#6b6b6b', text: '#3a3a3a',
          heading: '#1a1a1a', accent: '#0d9488', link: '#2563eb'
        }
      },
      fontFamily: {
        sans: ['Ubuntu','Inter','Segoe UI','PingFang SC','Microsoft YaHei','system-ui','sans-serif'],
        mono: ['JetBrains Mono','Fira Code','Cascadia Code','monospace']
      }
    }
  },
  plugins: [
    ({ addVariant }) => addVariant('light', '.light &')
  ]
}
