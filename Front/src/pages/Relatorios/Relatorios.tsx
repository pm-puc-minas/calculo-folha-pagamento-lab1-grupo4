import React, { useState } from 'react'
import './Relatorios.css'

async function downloadPdfFromServer(): Promise<void> {
  try {
    const res = await fetch('http://localhost:8080/api/folhas/export/pdf')
    if (!res.ok) throw new Error('Falha ao baixar PDF do servidor')
    const blob = await res.blob()

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

function Relatorios() {
  const [baixando, setBaixando] = useState(false)
  const handleExportCsv = () => {
    alert('Exportar CSV - função não implementada ainda')
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
    <div className="pagina-relatorios">
      <h1>Relatórios</h1>
      <p>Gerar relatórios e exportar dados da folha de pagamento.</p>

      <div className="acoes-relatorios">
        <button onClick={handleExportCsv}>Exportar CSV</button>
        <button onClick={handleDownloadServidor} disabled={baixando}>{baixando ? 'Baixando...' : 'Exportar PDF (servidor)'}</button>
      </div>
    </div>
  )
}

export default Relatorios
