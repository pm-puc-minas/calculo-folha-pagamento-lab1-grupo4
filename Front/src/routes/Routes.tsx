import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

import App from '../App'
import Login from '../pages/Auth/Login'
import Register from '../pages/Auth/Register'

export const AppRoutes = () => {
  return (
    <Router>    
        <Routes>
            <Route path="/" element={<App />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
        </Routes>
    </Router>
  )
}   