import { Building2, User, Lock } from "lucide-react"
import { useState } from "react"
import './Auth.css'
import apiFetch from '../../services/api'

function Login() {
  const [matricula, setMatricula] = useState("")
  const [senha, setSenha] = useState("")
  const [mensagem, setMensagem] = useState("")
  const [carregando, setCarregando] = useState(false)
  const [erro, setErro] = useState(false)

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!matricula || !senha) {
      setMensagem("Preencha todos os campos!")
      setErro(true)
      return
    }

    setCarregando(true)
    setErro(false)
    setMensagem("")

    try {
      const response = await apiFetch("http://localhost:8080/api/auth/login", {
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
        setMensagem("Login realizado com sucesso!")
        setErro(false)
        // Armazena as informações do usuário
        localStorage.setItem("usuario", JSON.stringify(data))
        // Redireciona para dashboard
        setTimeout(() => {
          window.location.href = data.isAdmin ? "/create-funcionarios" : "/dashboard"
        }, 1000)
      } else {
        setMensagem(data.mensagem || "Falha ao fazer login")
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
          <p className="subtitulo-login">Faça login para acessar o sistema</p>
        </div>

        <div className="card-login">
          <div className="cabecalho-card">
            <h2 className="titulo-card">Acesso ao Sistema</h2>
            <p className="descricao-card">
              Digite sua matrícula e senha para continuar
            </p>
          </div>

          <form onSubmit={handleLogin} className="formulario-card">
  
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
                  placeholder="Digite sua senha"
                  className="input-card"
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
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
              type="submit" 
              className="botao-card"
              disabled={carregando}
            >
              {carregando ? "Entrando..." : "Entrar"}
            </button>
          </form>

          <div className="cadastro-card">
            <p className="texto-cadastro">Ainda não possui cadastro? <strong><a href="/register">cadastrar</a></strong></p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login
