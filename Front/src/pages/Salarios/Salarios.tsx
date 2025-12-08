import React, { useEffect, useState } from 'react'
import './Salarios.css'
import apiFetch from '../../services/api'

type Funcionario = { id: number; nome: string; matricula: string }
type Folha = { 
  id: number; 
  funcionario: Funcionario; 
  mesReferencia: string; 
  salarioLiquido?: number; 
  salarioBruto: number;
  totalDescontos?: number;
  totalBeneficios?: number;
  inss?: number;
  irrf?: number;
  fgts?: number;
  valeTransporte?: number;
}

function Salarios() {
  const [salarios, setSalarios] = useState<Folha[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const usuarioInfo = React.useMemo(() => {
    if (typeof window === 'undefined') return null
    try { return JSON.parse(localStorage.getItem('usuario') || 'null') } catch { return null }
  }, [])

  const sortedSalarios = React.useMemo(() => {
    return [...salarios].sort((a, b) => {
      const dateA = a.mesReferencia ? new Date(a.mesReferencia).getTime() : 0
      const dateB = b.mesReferencia ? new Date(b.mesReferencia).getTime() : 0
      return dateB - dateA
    })
  }, [salarios])

  const minhasFolhas = React.useMemo(() => {
    if (!usuarioInfo) return sortedSalarios
    if (usuarioInfo.isAdmin) return sortedSalarios
    return sortedSalarios.filter(f => f.funcionario?.matricula === usuarioInfo.matricula)
  }, [sortedSalarios, usuarioInfo])

  useEffect(() => {
    const fetchSalarios = async () => {
      setLoading(true)
      try {
        const res = await apiFetch('http://localhost:8080/api/folhas')
        if (res.status === 401) throw new Error('Faça login para acessar seus salários')
        if (res.status === 403) throw new Error('Você não tem permissão para acessar esses dados')
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
          {sortedSalarios.length === 0 ? (
            <p>Nenhuma folha de pagamento encontrada.</p>
          ) : (
            <> 
              {usuarioInfo && (
                <div className="muted" style={{marginBottom:12}}>
                  Exibindo salários de {usuarioInfo.nome || 'seu perfil'} ({usuarioInfo.matricula})
                </div>
              )}
              <table className="tabela-salarios-page">
                <thead>
                  <tr>
                    <th>Funcionário</th>
                    <th>Matrícula</th>
                    <th>Período</th>
                    <th>Salário Bruto</th>
                    <th>INSS</th>
                    <th>IRRF</th>
                    <th>Vale Transporte</th>
                    <th>FGTS</th>
                    <th>Salário Líquido</th>
                  </tr>
                </thead>
                <tbody>
                  {minhasFolhas.map((s) => (
                    <tr key={s.id}>
                      <td>{s.funcionario?.nome || '—'}</td>
                      <td>{s.funcionario?.matricula || '—'}</td>
                      <td>{s.mesReferencia ? new Date(s.mesReferencia).toLocaleDateString() : '—'}</td>
                      <td>R$ {(s.salarioBruto ?? 0).toFixed(2)}</td>
                      <td>R$ {(s.inss ?? 0).toFixed(2)}</td>
                      <td>R$ {(s.irrf ?? 0).toFixed(2)}</td>
                      <td>R$ {(s.valeTransporte ?? 0).toFixed(2)}</td>
                      <td>R$ {(s.fgts ?? 0).toFixed(2)}</td>
                      <td><strong>R$ {(s.salarioLiquido ?? s.salarioBruto ?? 0).toFixed(2)}</strong></td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </>
          )}
        </div>
      )}
    </div>
  )
}

export default Salarios
