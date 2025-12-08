import { Link } from 'react-router-dom'
import './Header.css'

function Header() {
  let usuarioNome = ''
  try {
    const u = typeof window !== 'undefined' ? localStorage.getItem('usuario') : null
    if (u) usuarioNome = JSON.parse(u).nome || JSON.parse(u).matricula || ''
  } catch (e) {
    usuarioNome = ''
  }

  const handleLogout = () => {
    localStorage.removeItem('usuario')
    window.location.href = '/login'
  }

  return (
    <header className="app-header">
      <div className="container header-inner">
        <div className="logo-area">
          <Link to="/" className="logo-link">Sistema Folha</Link>
        </div>
        <div />
      </div>
    </header>
  )
}

export default Header
