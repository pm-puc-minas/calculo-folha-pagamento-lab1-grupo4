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
      <div className="logo-area">
        <Link to="/" className="logo-link">Sistema Folha</Link>
      </div>
      <nav className="nav-links">
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/create-funcionarios">Funcionários</Link>
        <Link to="/folhas">Folhas</Link>
      </nav>
      <div className="user-area">
        {usuarioNome ? (<>
          <span className="user-name">{usuarioNome}</span>
          <button className="logout-button" onClick={handleLogout}>Sair</button>
        </>) : (
          <Link to="/login">Entrar</Link>
        )}
      </div>
    </header>
  )
}

export default Header
