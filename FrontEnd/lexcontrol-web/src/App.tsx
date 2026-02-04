import { QueryClient, QueryClientProvider, useQuery } from '@tanstack/react-query';
import axios from 'axios';
import {  Users, Wallet, AlertCircle } from 'lucide-react'; 


const api = axios.create({ baseURL: 'http://localhost:8080' });
const queryClient = new QueryClient();


function Dashboard() {
  
  const { data: clientes, isLoading, error } = useQuery({
    queryKey: ['clientes'],
    queryFn: async () => {
      const res = await api.get('/clientes');
      return res.data;
    }
  });

  if (isLoading) return <div className="p-10 text-blue-600 animate-pulse">Carregando dados do Java...</div>;
  if (error) return <div className="p-10 text-red-500 font-bold flex gap-2"><AlertCircle /> Erro ao conectar na API! O Java tá on?</div>;

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-4xl mx-auto">
        <header className="mb-8 flex items-center gap-3">
          <div className="p-3 bg-blue-600 rounded-lg shadow-lg">
            <Users className="text-white w-6 h-6" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-slate-800">LexControl</h1>
            <p className="text-slate-500">Gestão Jurídica Inteligente</p>
          </div>
        </header>

        <div className="grid gap-4">
          {clientes?.length === 0 ? (
            <div className="p-8 text-center text-slate-400 bg-white rounded-xl border border-dashed border-slate-300">
              Nenhum cliente encontrado no banco.
            </div>
          ) : (
            clientes?.map((cliente: any) => (
              <div key={cliente.id} className="bg-white p-6 rounded-xl shadow-sm border border-slate-100 hover:shadow-md transition-all flex justify-between items-center group">
                <div>
                  <h3 className="font-semibold text-lg text-slate-800 group-hover:text-blue-600 transition-colors">
                    {cliente.nomeCliente}
                  </h3>
                  <div className="flex items-center gap-2 text-sm text-slate-500 mt-1">
                    <span className="bg-slate-100 px-2 py-0.5 rounded text-xs uppercase font-bold tracking-wide">
                      {cliente.causa}
                    </span>
                    <span>• CPF: {cliente.cpf}</span>
                  </div>
                </div>
                
                <div className="text-right">
                  <div className="flex items-center gap-1 justify-end font-mono font-medium text-slate-700">
                    <Wallet size={16} className="text-slate-400" />
                    R$ {cliente.valorCausa?.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </div>
                  <span className={`inline-block mt-2 px-3 py-1 rounded-full text-xs font-bold ${
                    cliente.statusPagamento === 'PAGO' ? 'bg-green-100 text-green-700' :
                    cliente.statusPagamento === 'PENDENTE' ? 'bg-yellow-100 text-yellow-700' :
                    'bg-red-100 text-red-700'
                  }`}>
                    {cliente.statusPagamento}
                  </span>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}


export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Dashboard />
    </QueryClientProvider>
  );
}