import React, { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import '../../Funcionarios/Funcionarios.css'
import apiFetch from '../../../services/api'

type Funcionario = {
  id: number
  nome: string
  matricula: string
  cpf?: string
  cargo?: string
  departamento?: string
  salarioBruto?: number
}

function FuncionarioPerfil() {
  const params = useParams()
  const navigate = useNavigate()
  const [funcionario, setFuncionario] = useState<Funcionario | null>(null)
  const [loading, setLoading] = useState(false)

  // decide id to load: param id if present, otherwise use logged user
  const usuario = typeof window !== 'undefined' ? localStorage.getItem('usuario') : null
  let usuarioObj: any = null
  try { if (usuario) usuarioObj = JSON.parse(usuario) } catch {}

  const idToLoad = params.id || usuarioObj?.id

  useEffect(() => {
    if (!idToLoad) { navigate('/login'); return }
    const fetchData = async () => {
      setLoading(true)
      try {
          const res = await apiFetch(`http://localhost:8080/api/funcionarios/${idToLoad}`)
        if (res.ok) {
          const data = await res.json()
          setFuncionario(data)
        } else {
          setFuncionario(null)
        }
      } catch (err) { console.error(err) }
      finally { setLoading(false) }
    }
    fetchData()
  }, [idToLoad])

  if (loading) return <div className="container"><p>Carregando...</p></div>

  if (!funcionario) return <div className="container"><p>Funcionário não encontrado</p></div>

  return (
    <div className="pagina-funcionario container">
      <div className="card">
        <h1>{funcionario.nome}</h1>
        <div className="muted">Matrícula: {funcionario.matricula}</div>
        <div style={{marginTop:12}}>
          <p><strong>CPF:</strong> {funcionario.cpf}</p>
          <p><strong>Cargo:</strong> {funcionario.cargo}</p>
          <p><strong>Departamento:</strong> {funcionario.departamento}</p>
          <p><strong>Salário bruto:</strong> R$ { (funcionario.salarioBruto || 0).toFixed(2) }</p>
        </div>
      </div>
    </div>
  )
}

export default FuncionarioPerfil
