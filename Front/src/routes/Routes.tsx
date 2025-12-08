import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

import App from '../App'
import Login from '../pages/Auth/Login'
import Register from '../pages/Auth/Register'
import CreateFuncionario from '../pages/Funcionarios/Create/CreateFuncionario'
import Dashboard from '../pages/Dashboard/Dashboard'
import Folhas from '../pages/Folhas/Folhas'
import Salarios from '../pages/Salarios/Salarios'
import Relatorios from '../pages/Relatorios/Relatorios'
import Header from '../components/Header'

export const AppRoutes = () => {
  return (
    <Router>
      <Header />
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/create-funcionarios" element={<CreateFuncionario />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/folhas" element={<Folhas />} />
        <Route path="/salarios" element={<Salarios />} />
        <Route path="/relatorios" element={<Relatorios />} />
      </Routes>
    </Router>
  )
}