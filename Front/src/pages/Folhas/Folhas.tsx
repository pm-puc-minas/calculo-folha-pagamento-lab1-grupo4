import React, { useEffect, useState } from 'react'
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
}

function Folhas() {
  const [folhas, setFolhas] = useState<Folha[]>([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    const fetchFolhas = async () => {
      setLoading(true)
      try {
        const res = await fetch('http://localhost:8080/api/folhas')
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
    <div className="pagina-folhas">
      <h1>Folhas de Pagamento</h1>
      {loading && <p>Carregando...</p>}
      {error && <p className="erro">{error}</p>}
      {!loading && !error && (
        <ul className="lista-folhas">
          {folhas.map((f) => (
            <li key={f.id} className="item-folha">
              <div>
                <strong>{f.funcionario?.nome || '—'}</strong>
                <div className="meta">Matrícula: {f.funcionario?.matricula || '—'}</div>
              </div>
              <div className="meta">Período: {new Date(f.mesReferencia).toLocaleDateString()}</div>
              <div className="valor">R$ { (f.salarioLiquido ?? f.salarioBruto).toFixed(2) }</div>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}

export default Folhas
