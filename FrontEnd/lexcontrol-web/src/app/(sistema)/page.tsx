import { Users, DollarSign, Scale, ArrowRight, Bell, FileText } from 'lucide-react';import Link from 'next/link';

export default function DashboardPage() {
  return (
    <div className="space-y-10">

      <div className="flex justify-between items-end">
        <div>
          <h1 className="text-3xl font-bold text-white mb-2">Bom dia, Dr. Pedro Stein</h1>
          <p className="text-slate-400">Aqui está o resumo do seu escritório hoje.</p>
          <p className="text-slate-400">Aqui é nossa versao teste</p>
        </div>
        <button className="p-2 bg-slate-800 rounded-full text-slate-400 hover:text-white hover:bg-slate-700 transition relative">
          <Bell size={20} />
          <span className="absolute top-2 right-2 w-2 h-2 bg-red-500 rounded-full border-2 border-slate-800"></span>
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

        <div className="bg-slate-900/50 backdrop-blur border border-slate-800 p-6 rounded-2xl hover:border-blue-500/30 transition-all group">
          <div className="flex justify-between items-start mb-4">
            <div className="p-3 bg-blue-500/10 text-blue-400 rounded-xl group-hover:bg-blue-500 group-hover:text-white transition-colors">
              <Users size={24} />
            </div>
            <span className="text-xs font-bold bg-green-500/10 text-green-400 px-2 py-1 rounded">+2 essa semana</span>
          </div>
          <p className="text-slate-500 text-sm">Clientes Ativos</p>
          <h3 className="text-3xl font-bold text-white mt-1">124</h3>
        </div>


        <div className="bg-slate-900/50 backdrop-blur border border-slate-800 p-6 rounded-2xl hover:border-green-500/30 transition-all group">
          <div className="flex justify-between items-start mb-4">
            <div className="p-3 bg-green-500/10 text-green-400 rounded-xl group-hover:bg-green-500 group-hover:text-white transition-colors">
              <DollarSign size={24} />
            </div>
          </div>
          <p className="text-slate-500 text-sm">Honorários (Mês)</p>
          <h3 className="text-3xl font-bold text-white mt-1">R$ 12.450</h3>
        </div>


        <div className="bg-slate-900/50 backdrop-blur border border-slate-800 p-6 rounded-2xl hover:border-purple-500/30 transition-all group">
          <div className="flex justify-between items-start mb-4">
            <div className="p-3 bg-purple-500/10 text-purple-400 rounded-xl group-hover:bg-purple-500 group-hover:text-white transition-colors">
              <Scale size={24} />
            </div>
            <span className="text-xs font-bold bg-red-500/10 text-red-400 px-2 py-1 rounded">3 Prazos hoje</span>
          </div>
          <p className="text-slate-500 text-sm">Processos em Andamento</p>
          <h3 className="text-3xl font-bold text-white mt-1">8</h3>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div className="bg-gradient-to-br from-blue-900/40 to-slate-900/40 border border-blue-800/30 rounded-3xl p-8 relative overflow-hidden">
          <div className="relative z-10">
            <h3 className="text-xl font-bold text-white mb-2">Cadastrar Novo Cliente</h3>
            <p className="text-slate-400 mb-6 max-w-sm">Inicie um novo atendimento registrando os dados e o processo.</p>
            <Link href="/clientes/cadastro" className="inline-flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white px-6 py-3 rounded-xl font-semibold transition-all">
              Iniciar Cadastro <ArrowRight size={18} />
            </Link>
          </div>

          <div className="absolute -bottom-10 -right-10 bg-blue-500/20 w-48 h-48 rounded-full blur-3xl" />
        </div>

        <div className="bg-slate-900/50 border border-slate-800 rounded-3xl p-8 flex flex-col justify-center items-center text-center">
          <div className="bg-slate-800 p-4 rounded-full mb-4">
            <FileText size={32} className="text-slate-500" />
          </div>
          <h3 className="text-lg font-semibold text-white">Módulo de Notas</h3>
          <p className="text-slate-500 text-sm mt-2">Em breve você poderá gerenciar notas fiscais por aqui.</p>
        </div>
      </div>
    </div>
  );
}