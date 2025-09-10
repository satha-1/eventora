import { Routes, Route, Link, Navigate } from 'react-router-dom'
import Login from './pages/Login'
import Register from './pages/Register'
import Events from './pages/Events'
import EventDetail from './pages/EventDetail'
import Dashboard from './pages/Dashboard'
import { useAuth } from './auth'

export default function App() {
  const { token, logout } = useAuth()
  return (
    <div style={{fontFamily:'system-ui',padding:16,maxWidth:1000,margin:'0 auto'}}>
      <header style={{display:'flex',gap:16,alignItems:'center',justifyContent:'space-between'}}>
        <h2><Link to="/">Eventora</Link></h2>
        <nav style={{display:'flex',gap:12}}>
          <Link to="/events">Events</Link>
          {!token && <Link to="/login">Login</Link>}
          {!token && <Link to="/register">Register</Link>}
          {token && <Link to="/dashboard">Dashboard</Link>}
          {token && <button onClick={logout}>Logout</button>}
        </nav>
      </header>
      <Routes>
        <Route path="/" element={<Navigate to="/events" />} />
        <Route path="/login" element={<Login/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/events" element={<Events/>} />
        <Route path="/events/:id" element={<EventDetail/>} />
        <Route path="/dashboard" element={token ? <Dashboard/> : <Navigate to="/login" />} />
      </Routes>
    </div>
  )
}
