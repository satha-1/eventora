import React, { createContext, useContext, useState, useEffect } from 'react'

type AuthCtx = {
  token: string | null
  name: string | null
  role: string | null
  login: (t:string, n:string, r:string)=>void
  logout: ()=>void
}

const Ctx = createContext<AuthCtx>({
  token: null, name: null, role: null, login: ()=>{}, logout: ()=>{}
})

export function AuthProvider({children}:{children: React.ReactNode}) {
  const [token,setToken] = useState<string|null>(localStorage.getItem('token'))
  const [name,setName] = useState<string|null>(localStorage.getItem('name'))
  const [role,setRole] = useState<string|null>(localStorage.getItem('role'))
  const login = (t:string, n:string, r:string)=>{
    localStorage.setItem('token',t); localStorage.setItem('name',n); localStorage.setItem('role',r)
    setToken(t); setName(n); setRole(r)
  }
  const logout = ()=>{
    localStorage.clear(); setToken(null); setName(null); setRole(null)
  }
  return <Ctx.Provider value={{token,name,role,login,logout}}>{children}</Ctx.Provider>
}

export function useAuth(){ return useContext(Ctx) }
