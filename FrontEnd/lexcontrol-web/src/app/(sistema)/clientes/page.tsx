'use client';

import { useQuery } from '@tanstack/react-query';
import { clienteService } from '@/modules/clientes/services/clienteService';
import { ClienteCard } from '@/modules/clientes/components/ClienteCard';
import { Users, AlertCircle, Loader2, Plus } from 'lucide-react';
import Link from 'next/link';

export default function ClientesPage() {
  const { data: clientes, isLoading, isError } = useQuery({
    queryKey: ['clientes'],
    queryFn: clienteService.listar,
  });

  return (
    <div className="max-w-5xl mx-auto space-y-8">
      
      {/* Header com Visual Premium */}
      <header className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-blue-500/10 text-blue-500 rounded-xl border border-blue-500/20 shadow-[0_0_15px_rgba(59,130,246,0.1)]">
            <Users size={24} />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white tracking-tight">Clientes</h1>
            <p className="text-slate-400 text-sm">Gerencie seus processos e atendimentos</p>
          </div>
        </div>
        
        <Link 
          href="/clientes/cadastro" 
          className="group bg-blue-600 hover:bg-blue-500 text-white px-6 py-3 rounded-xl font-semibold transition-all shadow-lg shadow-blue-900/20 flex items-center gap-2"
        >
          <Plus size={20} className="group-hover:rotate-90 transition-transform" />
          Novo Cliente
        </Link>
      </header>

      <div className="grid gap-4">
        
        {/* Loading State Dark */}
        {isLoading && (
          <div className="flex flex-col items-center justify-center py-20 text-slate-500 gap-3">
            <Loader2 className="animate-spin text-blue-500" size={32} /> 
            <p>Carregando sua lista...</p>
          </div>
        )}

        {/* Error State Dark */}
        {isError && (
          <div className="bg-red-500/10 border border-red-500/20 rounded-2xl p-6 flex flex-col items-center justify-center text-center gap-2 backdrop-blur-sm">
            <AlertCircle size={32} className="text-red-400" />
            <h3 className="font-semibold text-red-200">Erro de conexão</h3>
            <p className="text-red-300/70 text-sm">
              Não conseguimos buscar os clientes. Verifique se o Backend Java está rodando.
            </p>
          </div>
        )}

        {/* Lista de Clientes */}
        {clientes?.map((cliente) => (
          <ClienteCard key={cliente.id} cliente={cliente} />
        ))}

        {/* Empty State Dark */}
        {!isLoading && !isError && clientes?.length === 0 && (
          <div className="text-center py-20 bg-slate-900/30 rounded-3xl border border-dashed border-slate-800">
            <div className="inline-flex p-4 rounded-full bg-slate-800/50 mb-4 text-slate-600">
                <Users size={32} />
            </div>
            <p className="text-slate-400 font-medium">Nenhum cliente cadastrado ainda.</p>
            <p className="text-slate-600 text-sm mt-1">Clique no botão acima para adicionar o primeiro.</p>
          </div>
        )}
      </div>

    </div>
  );
}