'use client';

import { useForm } from 'react-hook-form';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import { ArrowLeft, Save, Loader2, UserPlus, AlertCircle } from 'lucide-react';
import Link from 'next/link';
import { clienteService } from '@/modules/clientes/services/clienteService';
import { Cliente } from '@/modules/clientes/types/cliente';

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
      router.push('/clientes');
    },
    onError: (error) => {
      // Agora com o telefone, esse erro deve sumir!
      alert('Erro ao salvar. Verifique o console para detalhes.');
      console.error(error);
    },
  });

  const onSubmit = (data: Cliente) => {
    mutation.mutate(data);
  };

  // ESTILOS (Design System Local)
  const inputStyle = "w-full p-4 bg-slate-950/50 border border-slate-800 rounded-xl text-slate-100 placeholder:text-slate-600 focus:outline-none focus:ring-2 focus:ring-blue-500/50 focus:border-blue-500 transition-all hover:border-slate-700";
  const labelStyle = "block text-sm font-medium text-slate-400 mb-2 ml-1";
  const sectionTitleStyle = "text-lg font-semibold text-white flex items-center gap-2 mb-6";

  return (
    <div className="max-w-4xl mx-auto">

      {/* Cabeçalho Flutuante */}
      <div className="mb-8 flex items-center justify-between">
        <div className="flex items-center gap-4">
          <Link href="/clientes" className="group p-3 bg-slate-900 hover:bg-slate-800 rounded-xl text-slate-400 hover:text-white transition-all border border-slate-800 hover:border-slate-700">
            <ArrowLeft size={20} className="group-hover:-translate-x-1 transition-transform" />
          </Link>
          <div>
            <h1 className="text-3xl font-bold text-white tracking-tight">Novo Cliente</h1>
            <p className="text-slate-400 text-sm mt-1">Preencha os dados para abrir um novo processo.</p>
          </div>
        </div>
      </div>

      {/* Card do Formulário */}
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="bg-slate-900/60 backdrop-blur-xl p-8 md:p-10 rounded-3xl border border-slate-800 shadow-2xl space-y-10"
      >
        {/* Seção 1: Dados Pessoais */}
        <div className="space-y-6">
          <h3 className={sectionTitleStyle}>
            <span className="w-8 h-8 rounded-lg bg-blue-500/10 text-blue-500 flex items-center justify-center text-sm font-bold">1</span>
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
                <span className="text-red-400 text-xs mt-2 flex items-center gap-1">
                  <AlertCircle size={12} /> {errors.nomeCliente.message}
                </span>
              )}
            </div>

            {/* MUDANÇA AQUI: Grid de 3 colunas para incluir o Telefone */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
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
              
              {/* NOVO CAMPO: Telefone */}
              <div>
                <label className={labelStyle}>Telefone</label>
                <input
                  {...register('telefone', { required: 'Telefone é obrigatório' })}
                  className={inputStyle}
                  placeholder="(11) 99999-9999"
                />
                {errors.telefone && (
                    <span className="text-red-400 text-xs mt-2 flex items-center gap-1">
                      <AlertCircle size={12} /> {errors.telefone.message}
                    </span>
                  )}
              </div>
            </div>
          </div>
        </div>

        <div className="h-px bg-slate-800/50" />

        {/* Seção 2: Processo */}
        <div className="space-y-6">
          <h3 className={sectionTitleStyle}>
            <span className="w-8 h-8 rounded-lg bg-purple-500/10 text-purple-500 flex items-center justify-center text-sm font-bold">2</span>
            Detalhes do Processo
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label className={labelStyle}>Tipo da Causa</label>
              <select {...register('causa', { required: true })} className={inputStyle}>
                <option value="" className="bg-slate-950 text-slate-500">Selecione o tipo...</option>
                <option value="TRABALHISTA" className="bg-slate-950">Trabalhista</option>
                <option value="CIVEL" className="bg-slate-950">Cível</option>
                <option value="CRIMINAL" className="bg-slate-950">Criminal</option>
                <option value="FAMILIA" className="bg-slate-950">Família</option>
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

        <div className="h-px bg-slate-800/50" />

        {/* Seção 3: Financeiro */}
        <div className="space-y-6">
          <h3 className={sectionTitleStyle}>
            <span className="w-8 h-8 rounded-lg bg-green-500/10 text-green-500 flex items-center justify-center text-sm font-bold">3</span>
            Financeiro
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label className={labelStyle}>Status Pagamento</label>
              <div className="relative">
                <select {...register('statusPagamento', { required: true })} className={`${inputStyle} appearance-none`}>
                  <option value="PENDENTE" className="bg-slate-950">Pendente</option>
                  <option value="PAGO" className="bg-slate-950">Pago</option>
                  <option value="ATRASADO" className="bg-slate-950">Atrasado</option>
                </select>
                <div className="absolute right-4 top-4 pointer-events-none text-slate-500">▼</div>
              </div>
            </div>

            <div>
              <label className={labelStyle}>Data de Vencimento</label>
              <input
                type="date"
                {...register('dataDeVencimento', { required: true })}
                className={inputStyle}
              />
            </div>

            <div>
              <label className={labelStyle}>Modelo de Pagamento</label>
              <div className="relative">
                <select {...register('modeloDePagamento', { required: true })} className={`${inputStyle} appearance-none`}>
                  <option value="" className="bg-slate-950">Selecione...</option>
                  <option value="A_VISTA" className="bg-slate-950">À vista</option>
                  <option value="PARCELADO" className="bg-slate-950">Parcelado</option>
                </select>
                <div className="absolute right-4 top-4 pointer-events-none text-slate-500">▼</div>
              </div>
            </div>

            {modeloDePagamento === 'PARCELADO' && (
              <div className="animate-in fade-in slide-in-from-top-2 duration-300">
                <label className={labelStyle}>Valor da Parcela (R$)</label>
                <input
                  type="number"
                  step="0.01"
                  {...register('valorParcela', { required: true, valueAsNumber: true })}
                  className={`${inputStyle} border-blue-500/30 bg-blue-900/10 text-blue-200`}
                />
              </div>
            )}
          </div>
        </div>

        {/* Botão Salvar (Footer) */}
        <div className="pt-6 border-t border-slate-800/50 flex justify-end">
          <button
            type="submit"
            disabled={mutation.isPending}
            className="w-full md:w-auto bg-blue-600 hover:bg-blue-500 text-white font-bold py-4 px-8 rounded-xl shadow-lg shadow-blue-900/20 transition-all transform hover:scale-[1.02] active:scale-[0.98] flex items-center justify-center gap-3 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none"
          >
            {mutation.isPending ? <Loader2 className="animate-spin" /> : <Save size={20} />}
            {mutation.isPending ? 'Salvando...' : 'Salvar Cliente'}
          </button>
        </div>
      </form>
    </div>
  );
}