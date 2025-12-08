import React, { useEffect, useState } from 'react'
import apiFetch from '../../services/api'
import './Folhas.css'

type Funcionario = {
  id: number
  nome: string
  matricula: string
}

type Folha = {
  id: number
  funcionario: Funcionario
  mesReferencia: string
  salarioBruto: number
  salarioLiquido?: number
  totalDescontos?: number
  totalBeneficios?: number
  inss?: number
  irrf?: number
  fgts?: number
  valeTransporte?: number
}

function Folhas() {
  const [folhas, setFolhas] = useState<Folha[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchFolhas = async () => {
      setLoading(true)
      try {
        const res = await apiFetch('http://localhost:8080/api/folhas')
        if (!res.ok) throw new Error('Erro ao carregar folhas')
        const data = await res.json()
        setFolhas(data)
      } catch (err: any) {
        setError(err.message || 'Erro desconhecido')
      } finally {
        setLoading(false)
      }
    }
    fetchFolhas()
  }, [])

  return (
    <div className="pagina-folhas container">
      <h1>Folhas de Pagamento</h1>
      {loading && <p>Carregando...</p>}
      {error && <p className="erro">{error}</p>}
      {!loading && !error && (
        <div className="card">
          {folhas.length === 0 ? (
            <p>Nenhuma folha de pagamento cadastrada ainda.</p>
          ) : (
            <table className="tabela-folhas">
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
                {folhas.map((f) => (
                  <tr key={f.id}>
                    <td>{f.funcionario?.nome || '—'}</td>
                    <td>{f.funcionario?.matricula || '—'}</td>
                    <td>{new Date(f.mesReferencia).toLocaleDateString('pt-BR')}</td>
                    <td>R$ {(f.salarioBruto ?? 0).toFixed(2)}</td>
                    <td>R$ {(f.inss ?? 0).toFixed(2)}</td>
                    <td>R$ {(f.irrf ?? 0).toFixed(2)}</td>
                    <td>R$ {(f.valeTransporte ?? 0).toFixed(2)}</td>
                    <td>R$ {(f.fgts ?? 0).toFixed(2)}</td>
                    <td><strong>R$ {(f.salarioLiquido ?? f.salarioBruto ?? 0).toFixed(2)}</strong></td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </div>
  )
}

export default Folhas
