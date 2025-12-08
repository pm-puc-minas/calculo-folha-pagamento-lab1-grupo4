import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'

function DetalheFolha() {
  const { id } = useParams()
  const [folha, setFolha] = useState<any>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (!id) return
    const fetchData = async () => {
      setLoading(true)
      try {
        const res = await fetch(`http://localhost:8080/api/folhas/${id}`)
        if (res.ok) setFolha(await res.json())
      } catch (err) { console.error(err) }
      finally { setLoading(false) }
    }
    fetchData()
  }, [id])

  if (loading) return <div className="container"><p>Carregando...</p></div>
  if (!folha) return <div className="container"><p>Folha não encontrada</p></div>

  return (
    <div className="pagina-folha container">
      <div className="card">
        <h1>Folha #{folha.id}</h1>
        <div className="muted">Funcionário: {folha.funcionario?.nome || '—'} (Mat.: {folha.funcionario?.matricula || '—'})</div>
        <div style={{marginTop:12}}>
          <p><strong>Período:</strong> {folha.mesReferencia}</p>
          <p><strong>Salário bruto:</strong> R$ { (folha.salarioBruto || 0).toFixed(2) }</p>
          <p><strong>Total descontos:</strong> R$ { (folha.totalDescontos || 0).toFixed(2) }</p>
          <p><strong>Total benefícios:</strong> R$ { (folha.totalBeneficios || 0).toFixed(2) }</p>
          <p><strong>Salário líquido:</strong> R$ { (folha.salarioLiquido || 0).toFixed(2) }</p>
        </div>
      </div>
    </div>
  )
}

export default DetalheFolha
