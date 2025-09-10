import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../auth'

export default function Register(){
  const [name,setName] = useState('New User')
  const [email,setEmail] = useState('user@example.com')
  const [password,setPassword] = useState('password')
  const [role,setRole] = useState('ATTENDEE')
  const [error,setError] = useState<string|null>(null)
  const nav = useNavigate()
  const { login } = useAuth()

  const submit = async (e:any)=>{
    e.preventDefault(); setError(null)
    try{
      const res = await fetch('/api/auth/register',{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({name,email,password,role})})
      if(!res.ok) throw new Error(await res.text())
      const data = await res.json()
      login(data.token, data.name, data.role)
      nav('/dashboard')
    }catch(err:any){ setError(err.message || 'Register failed') }
  }
  return (
    <form onSubmit={submit} style={{display:'grid',gap:8,maxWidth:400}}>
      <h3>Register</h3>
      {error && <div style={{color:'red'}}>{error}</div>}
      <input placeholder="Name" value={name} onChange={e=>setName(e.target.value)} />
      <input placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} />
      <input placeholder="Password" type="password" value={password} onChange={e=>setPassword(e.target.value)} />
      <select value={role} onChange={e=>setRole(e.target.value)}>
        <option>ATTENDEE</option>
        <option>ORGANIZER</option>
        <option>VENDOR</option>
      </select>
      <button>Create Account</button>
    </form>
  )
}
