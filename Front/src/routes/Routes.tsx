import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'

import App from '../App'
import Login from '../pages/Auth/Login'

export const AppRoutes = () => {
  return (
    <Router>    
        <Routes>
            <Route path="/" element={<App />} />
            <Route path="/login" element={<Login />} />
        </Routes>
    </Router>
  )
}   