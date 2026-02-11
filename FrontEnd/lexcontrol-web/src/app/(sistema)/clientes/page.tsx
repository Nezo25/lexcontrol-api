'use client';

import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { clienteService } from '@/modules/clientes/services/clienteService';
import { ClienteCard } from '@/modules/clientes/components/ClienteCard';
import { Cliente } from '@/modules/clientes/types/cliente';
import { Modal } from '@/shared/components/Modal';
import { Users, AlertCircle, Loader2, Plus, MapPin, DollarSign, FileText, User, Calendar, CreditCard } from 'lucide-react';
import Link from 'next/link';

export default function ClientesPage() {
  const { data: clientes, isLoading, isError } = useQuery({
    queryKey: ['clientes'],
    queryFn: clienteService.listar,
  });

  // Estado para controlar qual cliente está sendo exibido no Modal
  const [selectedCliente, setSelectedCliente] = useState<Cliente | null>(null);

  // Formatações auxiliares (Moeda e Data)
  const formatMoney = (val?: number) =>
    Number(val || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

  const formatDate = (dateString?: string) =>
    dateString ? new Date(dateString).toLocaleDateString('pt-BR', { timeZone: 'UTC' }) : '-';

  return (
    <div className="max-w-5xl mx-auto space-y-8">

      {/*der da Página */}
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
        {/*ding e Erros */}
        {isLoading && (
          <div className="flex flex-col items-center justify-center py-20 text-slate-500 gap-3">
            <Loader2 className="animate-spin text-blue-500" size={32} />
            <p>Carregando sua lista...</p>
          </div>
        )}

        {isError && (
          <div className="bg-red-500/10 border border-red-500/20 rounded-2xl p-6 flex flex-col items-center justify-center text-center gap-2 backdrop-blur-sm">
            <AlertCircle size={32} className="text-red-400" />
            <h3 className="font-semibold text-red-200">Erro de conexão</h3>
            <p className="text-red-300/70 text-sm">Não conseguimos buscar os clientes.</p>
          </div>
        )}

        {/*ta de Clientes */}
        {clientes?.map((cliente) => (
          <ClienteCard
            key={cliente.id}
            cliente={cliente}
            // AQUI: Ao clicar duas vezes, abre o modal com este cliente
            onDoubleClick={(c) => setSelectedCliente(c)}
          />
        ))}

        {/*ty State */}
        {!isLoading && !isError && clientes?.length === 0 && (
          <div className="text-center py-20 bg-slate-900/30 rounded-3xl border border-dashed border-slate-800">
            <div className="inline-flex p-4 rounded-full bg-slate-800/50 mb-4 text-slate-600">
              <Users size={32} />
            </div>
            <p className="text-slate-400 font-medium">Nenhum cliente cadastrado ainda.</p>
          </div>
        )}
      </div>

      {/* O MODAL DE EXIBIÇÃO (FICHA COMPLETA) --- */}
      <Modal
        isOpen={!!selectedCliente}
        onClose={() => setSelectedCliente(null)}
        title="Ficha do Cliente"
      >
        {selectedCliente && (
          <div className="space-y-6">

            {/*CABEÇALHO (Nome e Status) */}
            <div className="flex items-start justify-between pb-4 border-b border-slate-800">
              <div>
                <h3 className="text-2xl font-bold text-white flex items-center gap-2">
                  {selectedCliente.nomeCliente}
                </h3>
                <span className="text-slate-400 text-sm flex items-center gap-2 mt-1">
                  <FileText size={14} className="text-blue-500" /> Processo: {selectedCliente.causa}
                </span>
              </div>
              <div className={`px-4 py-1.5 rounded-full text-xs font-bold border flex items-center gap-2 ${selectedCliente.statusPagamento === 'PAGO'
                  ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20'
                  : 'bg-amber-500/10 text-amber-400 border-amber-500/20'
                }`}>
                <div className={`w-2 h-2 rounded-full ${selectedCliente.statusPagamento === 'PAGO' ? 'bg-emerald-400' : 'bg-amber-400'} animate-pulse`} />
                {selectedCliente.statusPagamento}
              </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

              {/*DADOS PESSOAIS (CPF, RG, Telefone) */}
              <div className="bg-slate-950/50 p-5 rounded-2xl border border-slate-800 space-y-4">
                <h4 className="text-sm font-bold text-blue-400 flex items-center gap-2 uppercase tracking-wider">
                  <User size={16} /> Dados Pessoais
                </h4>
                <div className="space-y-3 text-sm">
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <span className="text-slate-500 block text-xs mb-1">CPF</span>
                      <span className="text-slate-200 font-mono bg-slate-900 px-2 py-1rounded border border-slate-800/50 block w-fit">
                        {selectedCliente.cpf}
                      </span>
                    </div>
                    <div>
                      <span className="text-slate-500 block text-xs mb-1">RG</span>
                      <span className="text-slate-200 font-mono bg-slate-900 px-2 py-1 rounded border border-slate-800/50 block w-fit">
                        {selectedCliente.rg}
                      </span>
                    </div>
                  </div>
                  <div>
                    <span className="text-slate-500 block text-xs mb-1">Telefone</span>
                    <span className="text-slate-200 font-medium">{selectedCliente.telefone || 'Não informado'}</span>
                  </div>
                </div>
              </div>

              {/*FINANCEIRO & PROCESSO (Valores, Datas, Modelo) */}
              <div className="bg-slate-950/50 p-5 rounded-2xl border border-slate-800 space-y-4">
                <h4 className="text-sm font-bold text-emerald-400 flex items-center gap-2 uppercase tracking-wider">
                  <DollarSign size={16} /> Financeiro
                </h4>
                <div className="space-y-3 text-sm">
                  <div className="flex justify-between items-center border-b border-slate-800/50 pb-2">
                    <span className="text-slate-400">Valor da Causa</span>
                    <span className="text-slate-200 font-mono">{formatMoney(selectedCliente.valorCausa)}</span>
                  </div>
                  <div className="flex justify-between items-center border-b border-slate-800/50 pb-2">
                    <span className="text-slate-400">Honorários Totais</span>
                    <span className="text-emerald-400 font-mono font-bold">{formatMoney(selectedCliente.totalHonorarios)}</span>
                  </div>

                  <div className="grid grid-cols-2 gap-2 pt-1">
                    <div>
                      <span className="text-slate-500 block text-xs mb-1">Modelo</span>
                      <div className="flex items-center gap-1 text-slate-300">
                        <CreditCard size={12} />
                        {selectedCliente.modeloDePagamento === 'A_VISTA' ? 'À Vista' : 'Parcelado'}
                      </div>
                    </div>

                    {/*tra valor da parcela apenas se for Parcelado */}
                    {selectedCliente.modeloDePagamento === 'PARCELADO' && (
                      <div>
                        <span className="text-slate-500 block text-xs mb-1">Valor Parcela</span>
                        <span className="text-slate-300 font-mono">{formatMoney(selectedCliente.valorParcela)}</span>
                      </div>
                    )}
                  </div>

                  <div className="pt-2">
                    <span className="text-slate-500 block text-xs mb-1">Vencimento</span>
                    <div className="flex items-center gap-2 text-amber-400 bg-amber-500/10 px-2 py-1 rounded w-fit text-xs font-bold border border-amber-500/20">
                      <Calendar size={12} /> {formatDate(selectedCliente.dataDeVencimento)}
                    </div>
                  </div>
                </div>
              </div>

              {/*ENDEREÇO (Ocupa as 2 colunas se existir) */}
              <div className="bg-slate-950/50 p-5 rounded-2xl border border-slate-800 space-y-4 md:col-span-2">
                <h4 className="text-sm font-bold text-purple-400 flex items-center gap-2 uppercase tracking-wider">
                  <MapPin size={16} /> Localização
                </h4>

                {selectedCliente.endereco ? (
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-4 text-sm">
                    <div>
                      <span className="text-slate-500 block text-xs mb-1">Logradouro</span>
                      <span className="text-slate-200">{selectedCliente.endereco.logradouro}, {selectedCliente.endereco.numero}</span>
                      {selectedCliente.endereco.complemento && (
                        <span className="text-slate-400 text-xs block mt-0.5">({selectedCliente.endereco.complemento})</span>
                      )}
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <span className="text-slate-500 block text-xs mb-1">Bairro</span>
                        <span className="text-slate-300">{selectedCliente.endereco.bairro}</span>
                      </div>
                      <div>
                        <span className="text-slate-500 block text-xs mb-1">CEP</span>
                        <span className="text-slate-300 font-mono">{selectedCliente.endereco.cep}</span>
                      </div>
                    </div>

                    <div className="md:col-span-2 pt-2 border-t border-slate-800/50 flex gap-2">
                      <span className="text-slate-500 text-xs">Cidade/UF:</span>
                      <span className="text-slate-200 font-medium">{selectedCliente.endereco.cidade} / {selectedCliente.endereco.estado}</span>
                    </div>
                  </div>
                ) : (
                  <p className="text-slate-500 text-sm italic py-2">Endereço não cadastrado.</p>
                )}
              </div>

            </div>

            {/*ão de Fechar */}
            <div className="flex justify-end pt-4 border-t border-slate-800">
              <button
                onClick={() => setSelectedCliente(null)}
                className="bg-slate-800 hover:bg-slate-700 text-white px-8 py-3 rounded-xl font-bold transition-all shadow-lg hover:shadow-slate-900/50 active:scale-95"
              >
                Fechar
              </button>
            </div>
          </div>
        )}
      </Modal>

    </div>
  );
}