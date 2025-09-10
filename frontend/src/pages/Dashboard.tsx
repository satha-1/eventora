import { useAuth } from '../auth'
import { useEffect, useState } from 'react'
import { api } from '../api'

type RFQ = { id:number, eventId:number, status:string, details:string }
type Proposal = { id:number, rfqId:number, price:number, message:string, status:string }

export default function Dashboard(){
  const { name, role, token } = useAuth()
  const [title, setTitle] = useState('My New Event')
  const [location, setLocation] = useState('Colombo')
  const [price, setPrice] = useState(5000)
  const [category, setCategory] = useState('Nightouts')
  const [message, setMessage] = useState<string|null>(null)

  const [incoming, setIncoming] = useState<RFQ[]>([])
  const [myRfqs, setMyRfqs] = useState<RFQ[]>([])
  const [rfqId, setRfqId] = useState<number|''>('')
  const [proposalPrice, setProposalPrice] = useState(10000)
  const [proposalMsg, setProposalMsg] = useState('We can do this event')

  useEffect(()=>{
    if(!token) return
    if(role === 'ORGANIZER'){
      api.get('/api/rfqs/incoming', token!).then(setIncoming).catch(()=>{})
    }
    api.get('/api/rfqs/mine', token!).then(setMyRfqs).catch(()=>{})
  },[token, role])

  const createEvent = async (e:any)=>{
    e.preventDefault(); setMessage(null)
    try{
      const data = await api.post('/api/events', {
        title, description: 'Created via dashboard', category, subcategory: '',
        location, startAt: new Date().toISOString(), endAt: new Date(Date.now()+2*3600*1000).toISOString(),
        price, capacity: 100
      }, token||undefined)
      setMessage('Created event #' + data.id)
    }catch(err:any){ setMessage(err.message) }
  }

  const sendProposal = async (e:any)=>{
    e.preventDefault()
    if(!rfqId) return
    await api.post('/api/proposals', { rfqId: Number(rfqId), price: proposalPrice, message: proposalMsg }, token!)
    setMessage('Proposal sent')
  }

  return (
    <div style={{display:'grid',gap:12,maxWidth:800}}>
      <h3>Welcome, {name} ({role})</h3>

      {(role === 'ORGANIZER' || role === 'ADMIN') && (
        <form onSubmit={createEvent} style={{display:'grid',gap:8,border:'1px solid #ddd',padding:12,borderRadius:8}}>
          <h4>Create Event</h4>
          <input placeholder="Title" value={title} onChange={e=>setTitle(e.target.value)} />
          <input placeholder="Location" value={location} onChange={e=>setLocation(e.target.value)} />
          <select value={category} onChange={e=>setCategory(e.target.value)}>
            <option>Weddings</option><option>Birthdays</option><option>Corporate</option>
            <option>Couples</option><option>Families</option><option>Dayouts</option><option>Nightouts</option>
          </select>
          <input type="number" value={price} onChange={e=>setPrice(parseInt(e.target.value||'0'))} />
          <button>Create</button>
          {message && <div>{message}</div>}
        </form>
      )}

      {role === 'ORGANIZER' && (
        <div style={{border:'1px solid #ddd',padding:12,borderRadius:8}}>
          <h4>Incoming RFQs</h4>
          {incoming.length===0 ? <div>No RFQs yet</div> : incoming.map(r => (
            <div key={r.id} style={{padding:8,borderBottom:'1px solid #eee'}}>
              <div>RFQ #{r.id} for Event #{r.eventId}</div>
              <div>{r.details}</div>
            </div>
          ))}
        </div>
      )}

      {role === 'VENDOR' && (
        <form onSubmit={sendProposal} style={{display:'grid',gap:8,border:'1px solid #ddd',padding:12,borderRadius:8}}>
          <h4>Send Proposal to RFQ</h4>
          <input placeholder="RFQ ID" value={rfqId} onChange={e=>setRfqId(e.target.value as any)} />
          <input type="number" placeholder="Price" value={proposalPrice} onChange={e=>setProposalPrice(parseInt(e.target.value||'0'))} />
          <textarea placeholder="Message" value={proposalMsg} onChange={e=>setProposalMsg(e.target.value)} />
          <button>Send Proposal</button>
        </form>
      )}

      <div style={{border:'1px solid #ddd',padding:12,borderRadius:8}}>
        <h4>My RFQs</h4>
        {myRfqs.length===0 ? <div>No RFQs yet</div> : myRfqs.map(r => (
          <div key={r.id} style={{padding:8,borderBottom:'1px solid #eee'}}>RFQ #{r.id} for Event #{r.eventId} • {r.status}</div>
        ))}
      </div>
    </div>
  )
}
