import React, { useState } from 'react'
import './Relatorios.css'
import apiFetch from '../../services/api'

async function downloadPdfFromServer(): Promise<void> {
  try {
    const res = await apiFetch('http://localhost:8080/api/folhas/export/pdf')
    if (!res.ok) {
      const payload = await res.text()
      throw new Error('Falha ao baixar PDF: ' + (payload || res.statusText))
    }

    const contentType = res.headers.get('content-type') || ''
    if (!contentType.toLowerCase().includes('application/pdf')) {
      const txt = await res.text()
      throw new Error('Resposta inesperada do servidor: ' + txt)
    }

    const buffer = await res.arrayBuffer()
    if (!buffer || buffer.byteLength === 0) {
      throw new Error('PDF vazio retornado pelo servidor')
    }

    const blob = new Blob([buffer], { type: 'application/pdf' })

    // Extrai nome do arquivo do header Content-Disposition, se disponível
    const disposition = res.headers.get('Content-Disposition')
    let filename = 'folhas.pdf'
    if (disposition) {
      const match = /filename\*=UTF-8''(.+)$/.exec(disposition) || /filename="?([^";]+)"?/.exec(disposition)
      if (match && match[1]) filename = decodeURIComponent(match[1])
    }

    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = filename
    document.body.appendChild(a)
    a.click()
    a.remove()
    window.URL.revokeObjectURL(url)
  } catch (err: any) {
    alert('Erro ao baixar PDF: ' + (err?.message || err))
    console.error(err)
  }
}

async function exportCsvFromServer(): Promise<void> {
  try {
    const res = await apiFetch('http://localhost:8080/api/folhas')
    if (!res.ok) {
      const t = await res.text()
      throw new Error('Falha ao obter dados: ' + (t || res.statusText))
    }
    const data = await res.json()
    if (!Array.isArray(data) || data.length === 0) {
      alert('Nenhuma folha disponível para exportação')
      return
    }

    const headers = ['id','nome','matricula','mesReferencia','salarioBruto','totalDescontos','totalBeneficios','salarioLiquido']
    const rows = data.map((f: any) => ([
      f.id,
      f.funcionario?.nome || '',
      f.funcionario?.matricula || '',
      f.mesReferencia || '',
      (f.salarioBruto ?? '').toString(),
      (f.totalDescontos ?? '').toString(),
      (f.totalBeneficios ?? '').toString(),
      (f.salarioLiquido ?? '').toString()
    ]))

    const csv = [headers.join(','), ...rows.map(r => r.map((c: any) => '"'+String(c).replace(/"/g,'""')+'"').join(','))].join('\n')
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = 'folhas.csv'
    document.body.appendChild(a)
    a.click()
    a.remove()
    window.URL.revokeObjectURL(url)
  } catch (err: any) {
    alert('Erro ao exportar CSV: ' + (err?.message || err))
    console.error(err)
  }
}

function Relatorios() {
  const [baixando, setBaixando] = useState(false)
  const handleExportCsv = async () => {
    await exportCsvFromServer()
  }

  const handleDownloadServidor = async () => {
    setBaixando(true)
    try {
      await downloadPdfFromServer()
    } finally {
      setBaixando(false)
    }
  }

  return (
    <div className="pagina-relatorios container">
      <div className="card">
        <h1>Relatórios</h1>
        <p>Gerar relatórios e exportar dados da folha de pagamento.</p>

        <div className="acoes-relatorios">
          <button className="btn secondary" onClick={handleExportCsv}>Exportar CSV</button>
          <button className="btn" onClick={handleDownloadServidor} disabled={baixando}>{baixando ? 'Baixando...' : 'Exportar PDF (servidor)'}</button>
        </div>
      </div>
    </div>
  )
}

export default Relatorios
