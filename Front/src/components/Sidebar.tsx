import { Link } from 'react-router-dom'
import './Sidebar.css'

function Sidebar() {
  let usuarioObj: any = null
  try { const u = localStorage.getItem('usuario'); if (u) usuarioObj = JSON.parse(u) } catch {}

  const isAdmin = usuarioObj?.isAdmin || usuarioObj?.matricula === '0001'

  return (
    <aside className="app-sidebar">
      <div className="sidebar-top">
        <div className="logo">Sistema Folha</div>
        <nav className="sidebar-nav">
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/folhas">Folhas</Link>
          <Link to="/salarios">Salários</Link>
          <Link to="/relatorios">Relatórios</Link>
          <Link to="/funcionarios">Funcionários</Link>
        </nav>
      </div>

      <div className="sidebar-footer">
        {usuarioObj ? (
          <div className="user-block">
            <div className="user-name">{usuarioObj.nome}</div>
            <div className="user-matricula muted">{usuarioObj.matricula}</div>
            <div className="actions">
              <Link to={`/funcionarios/${usuarioObj.id}`} className="btn secondary">Meu Perfil</Link>
              {isAdmin && <Link to="/create-funcionarios" className="btn">Cadastrar</Link>}
            </div>
          </div>
        ) : (
          <Link to="/login" className="btn">Entrar</Link>
        )}
      </div>
    </aside>
  )
}

export default Sidebar
