import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth'

export default function Login(){
  const [email,setEmail] = useState('admin@eventora.lk')
  const [password,setPassword] = useState('admin123')
  const [error,setError] = useState<string|null>(null)
  const nav = useNavigate()
  const { login } = useAuth()

  const submit = async (e:any)=>{
    e.preventDefault(); setError(null)
    try{
      const res = await fetch('/api/auth/login',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({email,password})})
      if(!res.ok) throw new Error(await res.text())
      const data = await res.json()
      login(data.token, data.name, data.role)
      nav('/dashboard')
    }catch(err:any){ setError(err.message || 'Login failed') }
  }
  return (
    <form onSubmit={submit} style={{display:'grid',gap:8,maxWidth:400}}>
      <h3>Login</h3>
      {error && <div style={{color:'red'}}>{error}</div>}
      <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
      <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
      <button>Login</button>
    </form>
  )
}
