'use client';

import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { ArrowLeft, Save, Loader2, UserPlus } from 'lucide-react';
import Link from 'next/link';
import { clienteService } from '@/services/clienteService';
import { Cliente } from '@/types/cliente';

export default function NovoClientePage() {
  const router = useRouter();
  const queryClient = useQueryClient();

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm<Cliente>();

  const modeloDePagamento = watch('modeloDePagamento');

  const mutation = useMutation({
    mutationFn: clienteService.criar,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['clientes'] });
      router.push('/'); // Voltar para a Home
    },
    onError: (error) => {
      alert('Erro ao salvar. Verifique se o Back-end Java está rodando!');
      console.error(error);
    },
  });

  const onSubmit = (data: Cliente) => {
    mutation.mutate(data);
  };

  // Estilos comuns para não repetir código
  const inputStyle = "w-full p-3 bg-slate-950 border border-slate-800 rounded-lg text-slate-100 placeholder:text-slate-600 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition-all";
  const labelStyle = "block text-sm font-medium text-slate-400 mb-1";

  return (
    <div className="min-h-screen bg-slate-950 p-6 md:p-12 flex justify-center">
      <div className="w-full max-w-3xl">
        
        {/* Cabeçalho */}
        <div className="mb-8 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <Link href="/" className="p-3 bg-slate-900 hover:bg-slate-800 rounded-full text-slate-400 hover:text-blue-400 transition-colors border border-slate-800">
              <ArrowLeft size={20} />
            </Link>
            <div>
              <h1 className="text-3xl font-bold text-slate-100 tracking-tight">Novo Cliente</h1>
              <p className="text-slate-500 text-sm mt-1">Preencha os dados do processo</p>
            </div>
          </div>
          <div className="hidden md:block p-3 bg-blue-500/10 rounded-xl text-blue-400">
            <UserPlus size={24} />
          </div>
        </div>

        {/* Card do Formulário */}
        <form
          onSubmit={handleSubmit(onSubmit)}
          className="bg-slate-900/50 backdrop-blur-sm p-8 rounded-2xl shadow-2xl border border-slate-800 space-y-8"
        >
          {/* Seção: Dados Pessoais */}
          <div className="space-y-6">
            <h3 className="text-lg font-semibold text-blue-400 border-b border-slate-800 pb-2 flex items-center gap-2">
              Dados Pessoais
            </h3>
            
            <div className="grid grid-cols-1 gap-6">
              <div>
                <label className={labelStyle}>Nome Completo</label>
                <input
                  {...register('nomeCliente', { required: 'Nome é obrigatório' })}
                  className={inputStyle}
                  placeholder="Ex: João da Silva"
                />
                {errors.nomeCliente && (
                  <span className="text-red-400 text-xs mt-1 block">{errors.nomeCliente.message}</span>
                )}
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className={labelStyle}>CPF</label>
                  <input
                    {...register('cpf', { required: true })}
                    className={inputStyle}
                    placeholder="000.000.000-00"
                  />
                </div>
                <div>
                  <label className={labelStyle}>RG</label>
                  <input
                    {...register('rg', { required: true })}
                    className={inputStyle}
                    placeholder="00.000.000-0"
                  />
                </div>
              </div>
            </div>
          </div>

          {/* Seção: Processo */}
          <div className="space-y-6">
            <h3 className="text-lg font-semibold text-blue-400 border-b border-slate-800 pb-2">
              Detalhes do Processo
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="md:col-span-2">
                <label className={labelStyle}>Tipo da Causa</label>
                <select {...register('causa', { required: true })} className={inputStyle}>
                  <option value="" className="bg-slate-900">Selecione o tipo...</option>
                  <option value="TRABALHISTA" className="bg-slate-900">Trabalhista</option>
                  <option value="CIVEL" className="bg-slate-900">Cível</option>
                  <option value="CRIMINAL" className="bg-slate-900">Criminal</option>
                  <option value="FAMILIA" className="bg-slate-900">Família</option>
                </select>
              </div>

              <div>
                <label className={labelStyle}>Valor da Causa (R$)</label>
                <input
                  type="number"
                  step="0.01"
                  {...register('valorCausa', { required: true, valueAsNumber: true })}
                  className={inputStyle}
                  placeholder="0,00"
                />
              </div>

              <div>
                <label className={labelStyle}>Total Honorários (R$)</label>
                <input
                  type="number"
                  step="0.01"
                  {...register('totalHonorarios', { required: true, valueAsNumber: true })}
                  className={inputStyle}
                  placeholder="0,00"
                />
              </div>
            </div>
          </div>

          {/* Seção: Financeiro */}
          <div className="space-y-6">
            <h3 className="text-lg font-semibold text-blue-400 border-b border-slate-800 pb-2">
              Financeiro
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className={labelStyle}>Status Pagamento</label>
                <select {...register('statusPagamento', { required: true })} className={inputStyle}>
                  <option value="PENDENTE" className="bg-slate-900">Pendente</option>
                  <option value="PAGO" className="bg-slate-900">Pago</option>
                  <option value="ATRASADO" className="bg-slate-900">Atrasado</option>
                </select>
              </div>

              <div>
                <label className={labelStyle}>Data de Vencimento</label>
                <input
                  type="date"
                  {...register('dataVencimento', { required: true })}
                  className={inputStyle}
                />
              </div>

              <div>
                <label className={labelStyle}>Modelo de Pagamento</label>
                <select {...register('modeloDePagamento', { required: true })} className={inputStyle}>
                  <option value="" className="bg-slate-900">Selecione...</option>
                  <option value="AVISTA" className="bg-slate-900">À vista</option>
                  <option value="PARCELADO" className="bg-slate-900">Parcelado</option>
                </select>
              </div>

              {modeloDePagamento === 'PARCELADO' && (
                <div className="animate-in fade-in slide-in-from-top-2 duration-300">
                  <label className={labelStyle}>Valor da Parcela (R$)</label>
                  <input
                    type="number"
                    step="0.01"
                    {...register('valorParcela', { required: true, valueAsNumber: true })}
                    className={`${inputStyle} border-blue-900/50 bg-blue-950/20`} // Destaque sutil
                  />
                </div>
              )}
            </div>
          </div>

          {/* Botão Salvar */}
          <div className="pt-4">
            <button
              type="submit"
              disabled={mutation.isPending}
              className="w-full bg-blue-600 hover:bg-blue-500 text-white font-bold py-4 rounded-xl shadow-lg shadow-blue-900/20 transition-all transform hover:scale-[1.01] active:scale-[0.99] flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {mutation.isPending ? <Loader2 className="animate-spin" /> : <Save size={20} />}
              {mutation.isPending ? 'Salvando...' : 'Salvar Cliente'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}