import { Users, FileText, DollarSign, BarChart2, LogOut } from 'lucide-react'
import { Link } from 'react-router-dom'
import './Dashboard.css'

function Dashboard() {
  const usuario = typeof window !== 'undefined' ? localStorage.getItem('usuario') : null
  let nome = ''
  try {
    if (usuario) nome = JSON.parse(usuario).nome || JSON.parse(usuario).matricula || ''
  } catch (e) {
    nome = ''
  }

  const atalhos = [
    { title: 'Funcionários', desc: 'Gerenciar funcionários', icon: <Users size={28} />, to: '/create-funcionarios' },
    { title: 'Folhas', desc: 'Criar e visualizar folhas', icon: <FileText size={28} />, to: '/folhas' },
    { title: 'Salários', desc: 'Visualizar salários', icon: <DollarSign size={28} />, to: '/salarios' },
    { title: 'Relatórios', desc: 'Relatórios e exportação', icon: <BarChart2 size={28} />, to: '/relatorios' }
  ]

  return (
    <div className="pagina-dashboard container">
      <header className="cabecalho-dashboard">
        <div>
          <h1>Dashboard</h1>
          <p className="saudacao">Olá{nome ? `, ${nome}` : ''}! Aqui estão seus atalhos.</p>
        </div>
        <div className="acoes-header">
          <Link to="/" className="botao-header">Ir para site</Link>
          <a className="botao-header" href="#" onClick={() => { localStorage.removeItem('usuario'); window.location.href = '/login' }}>
            <LogOut size={16} /> Sair
          </a>
        </div>
      </header>

      <section className="grid-atalhos">
        {atalhos.map((a) => (
          <Link to={a.to} key={a.title} className="card-atalho">
            <div className="icone-atalho">{a.icon}</div>
            <div className="conteudo-atalho">
              <h3>{a.title}</h3>
              <p>{a.desc}</p>
            </div>
          </Link>
        ))}
      </section>

      <section className="salarios-recentes card">
        <h2>Salários Recentes</h2>
        <p className="descricao-pequena">Acesse a página de Salários para ver suas folhas de pagamento detalhadas</p>
      </section>
    </div>
  )
}

export default Dashboard
