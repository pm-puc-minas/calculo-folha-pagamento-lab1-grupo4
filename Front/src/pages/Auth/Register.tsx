import { Building2, User, Lock } from "lucide-react"
import './Auth.css'

function Register() {
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

          <div className="formulario-card">
            <div className="campo-card">
              <label htmlFor="matricula" className="rotulo-card">
                Nome completo
              </label>
              <div className="campo-input">
                <User className="icone-input" />
                <input
                  id="Nome completo"
                  type="text"
                  placeholder="Digite seu nome completo"
                  className="input-card"
                />
              </div>
            </div>


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
                />
              </div>
            </div>
            <div className="campo-card">
              <label htmlFor="senha" className="rotulo-card">
                Confirmar Senha
              </label>
              <div className="campo-input">
                <Lock className="icone-input" />
                <input
                  id="senha"
                  type="password"
                  placeholder="Digite sua senha"
                  className="input-card"
                />
              </div>
            </div>

            <button type="button" className="botao-card">
              Entrar
            </button>
          </div>

          <div className="cadastro-card">
            <p className="texto-cadastro">Já possui uma conta? <strong><a href="/login">Logar</a></strong></p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Register
