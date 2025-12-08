import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

import App from '../App'
import Login from '../pages/Auth/Login'
import Register from '../pages/Auth/Register'
import CreateFuncionario from '../pages/Funcionarios/Create/CreateFuncionario'
import Dashboard from '../pages/Dashboard/Dashboard'
import Folhas from '../pages/Folhas/Folhas'
import Salarios from '../pages/Salarios/Salarios'
import Relatorios from '../pages/Relatorios/Relatorios'
import FuncionariosList from '../pages/Funcionarios/List/FuncionariosList'
import FuncionarioPerfil from '../pages/Funcionarios/Perfil/FuncionarioPerfil'
import DetalheFolha from '../pages/Folhas/DetalheFolha'
import Header from '../components/Header'
import Sidebar from '../components/Sidebar'

export const AppRoutes = () => {
  return (
    <Router>
      <Header />
      <div className="app-layout">
        <Sidebar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<App />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/create-funcionarios" element={<CreateFuncionario />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/folhas" element={<Folhas />} />
            <Route path="/folhas/:id" element={<DetalheFolha />} />
            <Route path="/funcionarios" element={<FuncionariosList />} />
            <Route path="/funcionarios/:id" element={<FuncionarioPerfil />} />
            <Route path="/salarios" element={<Salarios />} />
            <Route path="/relatorios" element={<Relatorios />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}