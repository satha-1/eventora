import { useParams } from 'react-router-dom'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { useAuth } from '../auth'
import { api } from '../api'
import { useState } from 'react'

export default function EventDetail(){
  const { id } = useParams()
  const { token } = useAuth()
  const qc = useQueryClient()
  const [pkg, setPkg] = useState('single')
  const [rfqDetails, setRfqDetails] = useState('Need catering + photography')

  const { data } = useQuery({ queryKey: ['event', id], queryFn: ()=> api.get('/api/events/'+id) })

  const book = useMutation({
    mutationFn: (body:{eventId:number, packageType:string}) => api.post('/api/bookings', body, token||undefined)
  })

  const createRfq = useMutation({
    mutationFn: (body:{eventId:number, details:string}) => api.post('/api/rfqs', body, token||undefined),
    onSuccess: ()=> qc.invalidateQueries({queryKey:['my_rfqs']})
  })

  const [rfqId, setRfqId] = useState<number|''>('')
  const proposals = useQuery({
    queryKey:['proposals', rfqId],
    queryFn: ()=> rfqId ? api.get('/api/proposals/rfq/'+rfqId) : Promise.resolve([]),
    enabled: !!rfqId
  })

  const acceptProposal = useMutation({
    mutationFn: (pid:number)=> api.post('/api/proposals/'+pid+'/accept', {}, token||undefined)
  })

  const initPayment = useMutation({
    mutationFn: (body:{registrationId:number, amount:number})=> api.post('/api/payments/init', body, token||undefined)
  })

  if(!data) return <div>Loading...</div>
  return (
    <div style={{display:'grid',gap:12}}>
      <h3>{data.title}</h3>
      <div>{data.category} • {data.location}</div>
      <p>{data.description}</p>
      <div>Capacity: {data.capacity} • Price: Rs. {data.price?.toLocaleString?.()}</div>

      <div style={{display:'flex',gap:8}}>
        <select value={pkg} onChange={e=>setPkg(e.target.value)}>
          <option value="single">Single</option>
          <option value="couple">Couple</option>
          <option value="family">Family</option>
          <option value="VIP">VIP</option>
        </select>
        <button onClick={()=>book.mutate({eventId: Number(id), packageType: pkg})} disabled={!token}>
          {token ? 'Book Now' : 'Login to Book'}
        </button>
        {book.isSuccess && <span>Booking confirmed! (mock)</span>}
      </div>

      <div style={{borderTop:'1px solid #eee',paddingTop:12}}>
        <h4>Request for Quote (RFQ)</h4>
        <textarea value={rfqDetails} onChange={e=>setRfqDetails(e.target.value)} style={{width:'100%',minHeight:80}}/>
        <button disabled={!token} onClick={()=>createRfq.mutate({eventId:Number(id), details: rfqDetails})}>
          {token ? 'Send RFQ' : 'Login to Send RFQ'}
        </button>
      </div>

      <div style={{borderTop:'1px solid #eee',paddingTop:12}}>
        <h4>Check Proposals</h4>
        <div style={{display:'flex',gap:8}}>
          <input placeholder="Enter RFQ ID" value={rfqId} onChange={e=>setRfqId(e.target.value as any)} />
          <button onClick={()=>qc.invalidateQueries({queryKey:['proposals', rfqId]})}>Load</button>
        </div>
        <div style={{display:'grid',gap:8}}>
          {proposals.data?.map((p:any)=>(
            <div key={p.id} style={{border:'1px solid #ddd',padding:8,borderRadius:8}}>
              <div>Proposal #{p.id} • Price Rs. {p.price?.toLocaleString?.()}</div>
              <div>{p.message}</div>
              <button disabled={!token} onClick={async ()=>{
                const res = await acceptProposal.mutateAsync(p.id)
                // In a real app, we'd fetch the created registration id; for demo, assume 1st step and init payment with price
                const pi:any = await initPayment.mutateAsync({ registrationId: 1, amount: p.price })
                window.open(pi.redirectUrl, '_blank')
              }}>{token ? 'Accept & Pay (Sandbox)' : 'Login to Accept'}</button>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
