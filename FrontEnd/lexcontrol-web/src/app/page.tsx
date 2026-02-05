'use client';

import { useQuery } from '@tanstack/react-query';
import { clienteService } from '@/services/clienteService';
import { ClienteCard } from '@/components/clientes/ClienteCard';
import { Users, AlertCircle, Loader2 } from 'lucide-react';
import Link from 'next/link';

export default function ClientesPage() {
  const { data: clientes, isLoading, isError } = useQuery({
    queryKey: ['clientes'],
    queryFn: clienteService.listar,
  });

  return (
    <div className="min-h-screen bg-slate-50 p-8">
      <div className="max-w-5xl mx-auto space-y-8">
        
        {/* 1. O Header agora renderiza SEMPRE, independente de erro */}
        <header className="flex justify-between items-center">
          <div className="flex items-center gap-3">
            <div className="p-3 bg-blue-600 rounded-xl shadow-lg text-white">
              <Users size={24} />
            </div>
            <div>
              <h1 className="text-2xl font-bold text-slate-900">Clientes</h1>
              <p className="text-slate-500 text-sm">Gestão de Processos</p>
            </div>
          </div>
          
          <Link 
            href="/clientes/novo" 
            className="bg-blue-600 text-white px-4 py-2 rounded-lg font-medium hover:bg-blue-700 transition"
          >
            + Novo Cliente
          </Link>
        </header>

        <div className="grid gap-4">
          
          {isLoading && (
            <div className="flex justify-center py-10 text-slate-500">
              <Loader2 className="animate-spin mr-2" /> Carregando lista...
            </div>
          )}

          {isError && (
            <div className="bg-red-50 border border-red-200 rounded-xl p-6 flex flex-col items-center justify-center text-center gap-2">
              <AlertCircle size={32} className="text-red-500" />
              <h3 className="font-semibold text-red-900">Erro de conexão</h3>
              <p className="text-red-700 text-sm">
                Não conseguimos buscar os clientes. Verifique se o Backend Java está rodando.
              </p>
            </div>
          )}

          {/* Lista de Clientes (Só aparece se tiver dados) */}
          {clientes?.map((cliente) => (
            <ClienteCard key={cliente.id} cliente={cliente} />
          ))}

          {/* Estado vazio (sucesso, mas sem clientes) */}
          {!isLoading && !isError && clientes?.length === 0 && (
            <div className="text-center py-12 bg-white rounded-xl border border-dashed border-slate-300">
              <p className="text-slate-500">Nenhum cliente cadastrado ainda.</p>
            </div>
          )}
        </div>

      </div>
    </div>
  );
}