import { useState } from 'react'
import { ArrowLeft, Save, AlertCircle } from 'lucide-react'
import './CreateFuncionario.css'

function CreateFuncionario() {
  const [formData, setFormData] = useState({
    nome: '',
    cpf: '',
    matricula: '',
    cargo: '',
    departamento: '',
    salarioBruto: '',
    dataAdmissao: new Date().toISOString().split('T')[0],
    horasPrevistas: '',
    valeTransporte: false,
    alimentacao: false, // ADICIONADO
    numeroDependentes: 0
  })

  const [carregando, setCarregando] = useState(false)
  const [mensagem, setMensagem] = useState('')
  const [erro, setErro] = useState(false)

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value, type } = e.target
    const checked = type === 'checkbox' ? (e.target as HTMLInputElement).checked : undefined

    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }))
  }

  const validarCPF = (cpf: string): boolean => {
    const cpfLimpo = cpf.replace(/\D/g, '')
    return cpfLimpo.length === 11
  }

  const validarFormulario = () => {
    if (!formData.nome.trim()) {
      setMensagem('Nome é obrigatório')
      setErro(true)
      return false
    }
    if (!formData.cpf.trim()) {
      setMensagem('CPF é obrigatório')
      setErro(true)
      return false
    }
    if (!validarCPF(formData.cpf)) {
      setMensagem('CPF inválido (deve ter 11 dígitos)')
      setErro(true)
      return false
    }
    if (!formData.matricula.trim()) {
      setMensagem('Matrícula é obrigatória')
      setErro(true)
      return false
    }
    if (!formData.cargo.trim()) {
      setMensagem('Cargo é obrigatório')
      setErro(true)
      return false
    }
    if (!formData.departamento.trim()) {
      setMensagem('Departamento é obrigatório')
      setErro(true)
      return false
    }
    if (!formData.salarioBruto || parseFloat(formData.salarioBruto) <= 0) {
      setMensagem('Salário bruto deve ser maior que zero')
      setErro(true)
      return false
    }
    if (!formData.horasPrevistas || parseFloat(formData.horasPrevistas) <= 0) {
      setMensagem('Horas previstas devem ser maior que zero')
      setErro(true)
      return false
    }
    return true
  }

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (!validarFormulario()) {
      return
    }

    setCarregando(true)
    setMensagem('')
    setErro(false)

    try {
      const usuario = JSON.parse(localStorage.getItem('usuario') || '{}')

      const payload = {
        nome: formData.nome.trim(),
        cpf: formData.cpf.replace(/\D/g, ''),
        matricula: formData.matricula.trim(),
        cargo: formData.cargo.trim(),
        departamento: formData.departamento.trim(),
        salarioBruto: parseFloat(formData.salarioBruto),
        dataAdmissao: formData.dataAdmissao,
        horasPrevistas: parseFloat(formData.horasPrevistas),
        horasTrabalhadas: 0,
        valeTransporte: formData.valeTransporte,
        alimentacao: formData.alimentacao, // ADICIONADO
        numeroDependentes: parseInt(formData.numeroDependentes.toString()) || 0
      }

      const response = await fetch('http://localhost:8080/api/funcionarios', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${usuario.token || ''}`
        },
        body: JSON.stringify(payload)
      })

      if (response.ok) {
        setMensagem('Funcionário cadastrado com sucesso!')
        setErro(false)
        // Limpa o formulário
        setFormData({
          nome: '',
          cpf: '',
          matricula: '',
          cargo: '',
          departamento: '',
          salarioBruto: '',
          dataAdmissao: new Date().toISOString().split('T')[0],
          horasPrevistas: '',
          valeTransporte: false,
          alimentacao: false,
          numeroDependentes: 0
        })
        // Redireciona após 2 segundos
        setTimeout(() => {
          window.location.href = '/funcionarios'
        }, 2000)
      } else {
        const data = await response.json()
        setMensagem(data.message || 'Erro ao cadastrar funcionário')
        setErro(true)
      }
    } catch (erro) {
      setMensagem('Erro ao conectar com o servidor')
      setErro(true)
      console.error('Erro:', erro)
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="pagina-create-funcionario">
      <div className="container-create">
        <div className="header-create">
          <button className="btn-voltar" onClick={() => window.history.back()}>
            <ArrowLeft size={20} />
            Voltar
          </button>
          <h1>Cadastrar Novo Funcionário</h1>
        </div>

        <form onSubmit={handleSubmit} className="form-create">
          <div className="secao-formulario">
            <h2>Informações Pessoais</h2>

            <div className="grupo-campos">
              <div className="campo-completo">
                <label htmlFor="nome">Nome Completo *</label>
                <input
                  type="text"
                  id="nome"
                  name="nome"
                  value={formData.nome}
                  onChange={handleChange}
                  placeholder="Digite o nome completo"
                  disabled={carregando}
                  required
                />
              </div>
            </div>

            <div className="grupo-campos">
              <div className="campo-meio">
                <label htmlFor="cpf">CPF *</label>
                <input
                  type="text"
                  id="cpf"
                  name="cpf"
                  value={formData.cpf}
                  onChange={handleChange}
                  placeholder="000.000.000-00"
                  disabled={carregando}
                  required
                />
              </div>
              <div className="campo-meio">
                <label htmlFor="matricula">Matrícula *</label>
                <input
                  type="text"
                  id="matricula"
                  name="matricula"
                  value={formData.matricula}
                  onChange={handleChange}
                  placeholder="Ex: 0002"
                  disabled={carregando}
                  required
                />
              </div>
            </div>
          </div>

          <div className="secao-formulario">
            <h2>Informações Profissionais</h2>

            <div className="grupo-campos">
              <div className="campo-meio">
                <label htmlFor="cargo">Cargo *</label>
                <input
                  type="text"
                  id="cargo"
                  name="cargo"
                  value={formData.cargo}
                  onChange={handleChange}
                  placeholder="Ex: Desenvolvedor"
                  disabled={carregando}
                  required
                />
              </div>
              <div className="campo-meio">
                <label htmlFor="departamento">Departamento *</label>
                <input
                  type="text"
                  id="departamento"
                  name="departamento"
                  value={formData.departamento}
                  onChange={handleChange}
                  placeholder="Ex: TI"
                  disabled={carregando}
                  required
                />
              </div>
            </div>

            <div className="grupo-campos">
              <div className="campo-completo">
                <label htmlFor="dataAdmissao">Data de Admissão *</label>
                <input
                  type="date"
                  id="dataAdmissao"
                  name="dataAdmissao"
                  value={formData.dataAdmissao}
                  onChange={handleChange}
                  disabled={carregando}
                  required
                />
              </div>
            </div>
          </div>

          <div className="secao-formulario">
            <h2>Informações Financeiras</h2>

            <div className="grupo-campos">
              <div className="campo-meio">
                <label htmlFor="salarioBruto">Salário Bruto (R$) *</label>
                <input
                  type="number"
                  id="salarioBruto"
                  name="salarioBruto"
                  value={formData.salarioBruto}
                  onChange={handleChange}
                  placeholder="0.00"
                  step="0.01"
                  min="0"
                  disabled={carregando}
                  required
                />
              </div>
              <div className="campo-meio">
                <label htmlFor="horasPrevistas">Horas Previstas/Mês *</label>
                <input
                  type="number"
                  id="horasPrevistas"
                  name="horasPrevistas"
                  value={formData.horasPrevistas}
                  onChange={handleChange}
                  placeholder="160"
                  step="0.5"
                  min="0"
                  disabled={carregando}
                  required
                />
              </div>
            </div>
          </div>

          <div className="secao-formulario">
            <h2>Benefícios</h2>

            <div className="grupo-campos">
              <div className="campo-checkbox">
                <input
                  type="checkbox"
                  id="valeTransporte"
                  name="valeTransporte"
                  checked={formData.valeTransporte}
                  onChange={handleChange}
                  disabled={carregando}
                />
                <label htmlFor="valeTransporte">Vale Transporte</label>
              </div>
              
              <div className="campo-checkbox">
                <input
                  type="checkbox"
                  id="alimentacao"
                  name="alimentacao"
                  checked={formData.alimentacao}
                  onChange={handleChange}
                  disabled={carregando}
                />
                <label htmlFor="alimentacao">Vale Alimentação</label>
              </div>
            </div>
          </div>

          <div className="secao-formulario">
            <h2>Dependentes</h2>

            <div className="grupo-campos">
              <div className="campo-completo">
                <label htmlFor="numeroDependentes">Número de Dependentes</label>
                <input
                  type="number"
                  id="numeroDependentes"
                  name="numeroDependentes"
                  value={formData.numeroDependentes}
                  onChange={handleChange}
                  min="0"
                  max="20"
                  disabled={carregando}
                />
              </div>
            </div>
          </div>

          {mensagem && (
            <div className={`mensagem-feedback ${erro ? 'erro' : 'sucesso'}`}>
              <AlertCircle size={20} />
              <span>{mensagem}</span>
            </div>
          )}

          <div className="botoes-acao">
            <button
              type="button"
              className="btn-cancelar"
              onClick={() => window.history.back()}
              disabled={carregando}
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="btn-salvar"
              disabled={carregando}
            >
              <Save size={20} />
              {carregando ? 'Salvando...' : 'Cadastrar Funcionário'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default CreateFuncionario