import { Building2, User, Lock } from "lucide-react"
import { useState } from "react"
import './Auth.css'

function Register() {
  const [matricula, setMatricula] = useState("")
  const [senha, setSenha] = useState("")
  const [confirmarSenha, setConfirmarSenha] = useState("")
  const [mensagem, setMensagem] = useState("")
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro] = useState(false)

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault()
    
    // Validações
    if (!matricula || !senha || !confirmarSenha) {
      setMensagem("Preencha todos os campos!")
      setErro(true)
      return
    }

    if (senha.length < 4) {
      setMensagem("A senha deve ter no mínimo 4 caracteres")
      setErro(true)
      return
    }

    if (senha !== confirmarSenha) {
      setMensagem("As senhas não coincidem!")
      setErro(true)
      return
    }

    setCarregando(true)
    setErro(false)
    setMensagem("")

    try {
      const response = await fetch("http://localhost:8080/api/auth/registrar", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          matricula: matricula.trim(),
          senha: senha
        })
      })

      const data = await response.json()

      if (response.ok) {
        setMensagem(data.mensagem || "Senha cadastrada com sucesso!")
        setErro(false)
        // Redireciona para login após 2 segundos
        setTimeout(() => {
          window.location.href = "/login"
        }, 2000)
      } else {
        setMensagem(data.mensagem || "Erro ao cadastrar senha")
        setErro(true)
      }
    } catch (erro) {
      setMensagem("Erro ao conectar com o servidor")
      setErro(true)
      console.error("Erro:", erro)
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="pagina-login">
      <div className="container-login">
      
        <div className="cabecalho-login">
          <div className="logo-login">
            <Building2 className="icone-logo" />
          </div>
          <h1 className="titulo-login">Sistema de Folha de Pagamento</h1>
          <p className="subtitulo-login">Cadastre sua senha para acessar o sistema</p>
        </div>

        <div className="card-login">
          <div className="cabecalho-card">
            <h2 className="titulo-card">Ativar Conta</h2>
            <p className="descricao-card">
              Use a matrícula fornecida pelo RH e crie sua senha
            </p>
          </div>

          <div className="formulario-card">
            <div className="campo-card">
              <label htmlFor="matricula" className="rotulo-card">
                Matrícula
              </label>
              <div className="campo-input">
                <User className="icone-input" />
                <input
                  id="matricula"
                  type="text"
                  placeholder="Digite sua matrícula"
                  className="input-card"
                  value={matricula}
                  onChange={(e) => setMatricula(e.target.value)}
                  disabled={carregando}
                />
              </div>
            </div>

            <div className="campo-card">
              <label htmlFor="senha" className="rotulo-card">
                Senha
              </label>
              <div className="campo-input">
                <Lock className="icone-input" />
                <input
                  id="senha"
                  type="password"
                  placeholder="Crie sua senha (mínimo 4 caracteres)"
                  className="input-card"
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  disabled={carregando}
                />
              </div>
            </div>

            <div className="campo-card">
              <label htmlFor="confirmarSenha" className="rotulo-card">
                Confirmar Senha
              </label>
              <div className="campo-input">
                <Lock className="icone-input" />
                <input
                  id="confirmarSenha"
                  type="password"
                  placeholder="Confirme sua senha"
                  className="input-card"
                  value={confirmarSenha}
                  onChange={(e) => setConfirmarSenha(e.target.value)}
                  disabled={carregando}
                />
              </div>
            </div>

            {mensagem && (
              <div className={`mensagem-feedback ${erro ? "erro" : "sucesso"}`}>
                {mensagem}
              </div>
            )}

            <button 
              type="button"
              onClick={handleRegister}
              className="botao-card"
              disabled={carregando}
            >
              {carregando ? "Cadastrando..." : "Cadastrar Senha"}
            </button>
          </div>

          <div className="cadastro-card">
            <p className="texto-cadastro">
              Já possui senha cadastrada? <strong><a href="/login">Fazer login</a></strong>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Register