import { useQuery } from '@tanstack/react-query'
import { Link, useSearchParams } from 'react-router-dom'

export default function Events(){
  const [sp, setSp] = useSearchParams()
  const q = sp.get('q')||''
  const category = sp.get('category')||''

  const { data } = useQuery({
    queryKey: ['events', q, category],
    queryFn: async ()=>{
      const url = new URL('/api/events', window.location.origin)
      if(q) url.searchParams.set('q', q)
      if(category) url.searchParams.set('category', category)
      const res = await fetch(url)
      if(!res.ok) throw new Error(await res.text())
      return res.json()
    }
  })

  return (
    <div style={{display:'grid',gap:12}}>
      <h3>Explore Events</h3>
      <div style={{display:'flex',gap:8}}>
        <input placeholder="Search..." value={q} onChange={e=>{ sp.set('q', e.target.value); setSp(sp, {replace:true}) }} />
        <select value={category} onChange={e=>{ if(e.target.value) sp.set('category', e.target.value); else sp.delete('category'); setSp(sp, {replace:true}) }}>
          <option value="">All categories</option>
          <option>Weddings</option><option>Birthdays</option><option>Corporate</option>
          <option>Couples</option><option>Families</option><option>Dayouts</option><option>Nightouts</option>
        </select>
      </div>
      <div style={{display:'grid',gap:8}}>
        {data?.map((ev:any)=>(
          <div key={ev.id} style={{border:'1px solid #ddd',padding:12,borderRadius:8}}>
            <h4><Link to={'/events/'+ev.id}>{ev.title}</Link></h4>
            <div>{ev.category} • {ev.location}</div>
            <div>Price: Rs. {ev.price?.toLocaleString?.()}</div>
          </div>
        ))}
      </div>
    </div>
  )
}
