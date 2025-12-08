import React, { useEffect, useState } from 'react'
import apiFetch from '../../../services/api'
import { Link } from 'react-router-dom'
import '../../Funcionarios/Funcionarios.css'

type Funcionario = {
  id: number
  nome: string
  matricula: string
  cargo?: string
}

function FuncionariosList() {
  const [funcionarios, setFuncionarios] = useState<Funcionario[]>([])
  const [loading, setLoading] = useState(false)

  const usuario = typeof window !== 'undefined' ? localStorage.getItem('usuario') : null
  let isAdmin = false
  try { if (usuario) isAdmin = JSON.parse(usuario).isAdmin || JSON.parse(usuario).matricula === '0001' } catch {}

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true)
      try {
        const res = await apiFetch('http://localhost:8080/api/funcionarios')
        if (res.ok) {
          const data = await res.json()
          setFuncionarios(data)
        } else {
          console.error('Falha ao carregar funcionários', res.status)
        }
      } catch (err) {
        console.error(err)
      } finally { setLoading(false) }
    }
    fetchData()
  }, [])

  return (
    <div className="pagina-funcionarios container">
      <div className="card">
        <div className="row spaced" style={{justifyContent: 'space-between', alignItems: 'center'}}>
          <h1>Funcionários</h1>
          {isAdmin && <Link to="/create-funcionarios" className="btn">Cadastrar</Link>}
        </div>

        {loading ? <p>Carregando...</p> : (
          <ul className="lista-funcionarios">
            {funcionarios.map(f => (
              <li key={f.id} className="item-funcionario">
                <div>
                  <strong>{f.nome}</strong>
                  <div className="muted">Matrícula: {f.matricula} • {f.cargo || ' — '}</div>
                </div>
                <div>
                  <Link to={`/funcionarios/${f.id}`} className="btn secondary">Ver</Link>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  )
}

export default FuncionariosList
