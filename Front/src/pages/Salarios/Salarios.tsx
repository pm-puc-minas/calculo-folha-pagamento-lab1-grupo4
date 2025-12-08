import React, { useEffect, useState } from 'react'
import './Salarios.css'

type Funcionario = { id: number; nome: string; matricula: string }
type Folha = { id: number; funcionario: Funcionario; mesReferencia: string; salarioLiquido?: number; salarioBruto: number }

function Salarios() {
  const [salarios, setSalarios] = useState<Folha[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchSalarios = async () => {
      setLoading(true)
      try {
        const res = await fetch('http://localhost:8080/api/folhas')
        if (!res.ok) throw new Error('Falha ao obter salários')
        const data = await res.json()
        setSalarios(data)
      } catch (err: any) {
        setError(err.message || 'Erro desconhecido')
      } finally {
        setLoading(false)
      }
    }
    fetchSalarios()
  }, [])

  return (
    <div className="pagina-salarios container">
      <h1>Salários</h1>
      {loading && <p>Carregando salários...</p>}
      {error && <p className="erro">{error}</p>}
      {!loading && !error && (
        <div className="card">
          <table className="tabela-salarios-page">
            <thead>
              <tr>
                <th>Funcionário</th>
                <th>Matrícula</th>
                <th>Período</th>
                <th>Salário Líquido</th>
              </tr>
            </thead>
            <tbody>
              {salarios.map((s) => (
                <tr key={s.id}>
                  <td>{s.funcionario?.nome || '—'}</td>
                  <td>{s.funcionario?.matricula || '—'}</td>
                  <td>{new Date(s.mesReferencia).toLocaleDateString()}</td>
                  <td>R$ {(s.salarioLiquido ?? s.salarioBruto).toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}

export default Salarios
