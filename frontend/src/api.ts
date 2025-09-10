export const api = {
  async get(path: string, token?: string){
    const res = await fetch(path, { headers: token ? { Authorization: `Bearer ${token}` } : {} })
    if(!res.ok) throw new Error(await res.text())
    return res.json()
  },
  async post(path: string, body: any, token?: string){
    const res = await fetch(path, {
      method: 'POST',
      headers: { 'Content-Type':'application/json', ...(token ? { Authorization: `Bearer ${token}` } : {}) },
      body: JSON.stringify(body)
    })
    if(!res.ok) throw new Error(await res.text())
    return res.json()
  }
}
