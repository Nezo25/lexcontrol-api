'use client';

import { useUser } from '@/shared/context/UserContext';
import { WelcomeScreen } from '@/modules/clientes/components/WelcomeScreen';
import { Users, FileText, Bell, CheckCircle2, ShieldCheck, ArrowRight } from 'lucide-react';
import Link from 'next/link';

export default function DashboardPage() {
  const { userName, isLoaded } = useUser();

  // Evita piscar a tela enquanto carrega o LocalStorage
  if (!isLoaded) return null;

  // CENÁRIO A: Não tem nome -> Tela de Boas-Vindas
  if (!userName) {
    return <WelcomeScreen />;
  }

  // CENÁRIO B: Já tem nome -> Home de Apresentação
  return (
    <div className="space-y-12 animate-in fade-in slide-in-from-bottom-4 duration-700">

      {/* Header Hero */}
      <div className="flex justify-between items-start gap-4 border-b border-slate-800/50 pb-8">
        
        {/* Bloco de Texto */}
        <div className="space-y-2">
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-500/10 text-blue-400 text-xs font-bold uppercase tracking-wider border border-blue-500/20">
            <span className="w-2 h-2 rounded-full bg-blue-500 animate-pulse" />
            Sistema Online
          </div>
          <h1 className="text-3xl md:text-4xl font-bold text-white">
            Olá, <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-purple-400">{userName}</span> 👋
          </h1>
          <p className="text-slate-400 text-sm md:text-lg max-w-2xl mt-2">
            Este é o <b>LexControl</b>. Seu painel central para gestão jurídica eficiente, segura e moderna.
          </p>
        </div>

        {/* Modalzinho de Sininho - Agora com 'shrink-0' para não esmagar */}
        <div className="relative group shrink-0">
          <button className="p-3 md:p-4 bg-slate-900 border border-slate-800 rounded-2xl text-slate-400 hover:text-white hover:border-slate-700 transition-all shadow-xl">
            <Bell size={20} className="md:w-6 md:h-6" />
            <span className="absolute top-2 right-2 md:top-3 md:right-3 w-3 h-3 bg-red-500 rounded-full border-2 border-slate-900 animate-bounce"></span>
          </button>

          {/* Tooltip/Modal Flutuante */}
          <div className="absolute right-0 mt-4 w-64 md:w-72 bg-slate-900/95 backdrop-blur-xl border border-slate-800 p-4 rounded-2xl shadow-2xl opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-300 translate-y-2 group-hover:translate-y-0 z-50">
            <h4 className="text-white font-bold mb-1">Notificações</h4>
            <p className="text-sm text-slate-400">
              Bem-vindo(a) ao time, <b>{userName}</b>! O sistema está pronto para uso.
            </p>
          </div>
        </div>
      </div>

      {/*Grid de Funcionalidades (Apresentação) */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">

        {/* Card: Módulo Clientes */}
        <div className="group bg-gradient-to-br from-slate-900 to-slate-950 border border-slate-800 p-8 rounded-3xl hover:border-blue-500/30 transition-all relative overflow-hidden">
          <div className="absolute top-0 right-0 p-8 opacity-10 group-hover:opacity-20 transition-opacity">
            <Users size={120} />
          </div>

          <div className="relative z-10 space-y-6">
            <div className="w-14 h-14 bg-blue-500/10 rounded-2xl flex items-center justify-center text-blue-500 group-hover:scale-110 transition-transform">
              <Users size={32} />
            </div>
            <div>
              <h3 className="text-2xl font-bold text-white mb-2">Gestão de Clientes</h3>
              <p className="text-slate-400 leading-relaxed">
                Cadastre processos, acompanhe status de honorários e organize toda a base de contatos do escritório em um só lugar.
              </p>
            </div>
            <ul className="space-y-3">
              <li className="flex items-center gap-3 text-sm text-slate-300">
                <CheckCircle2 size={16} className="text-green-500" /> Cadastro completo (Pessoa Física)
              </li>
              <li className="flex items-center gap-3 text-sm text-slate-300">
                <CheckCircle2 size={16} className="text-green-500" /> Controle financeiro por processo
              </li>
            </ul>
            <Link href="/clientes" className="inline-flex items-center gap-2 text-blue-400 font-bold hover:text-blue-300 transition-colors">
              Acessar Módulo <ArrowRight size={16} />
            </Link>
          </div>
        </div>

        {/*Card: Módulo Notas (Mural) */}
        <div className="group bg-gradient-to-br from-slate-900 to-slate-950 border border-slate-800 p-8 rounded-3xl hover:border-amber-500/30 transition-all relative overflow-hidden">
          <div className="absolute top-0 right-0 p-8 opacity-10 group-hover:opacity-20 transition-opacity">
            <FileText size={120} />
          </div>

          <div className="relative z-10 space-y-6">
            <div className="w-14 h-14 bg-amber-500/10 rounded-2xl flex items-center justify-center text-amber-500 group-hover:scale-110 transition-transform">
              <FileText size={32} />
            </div>
            <div>
              <h3 className="text-2xl font-bold text-white mb-2">Mural de Avisos</h3>
              <p className="text-slate-400 leading-relaxed">
                Um espaço colaborativo para a equipe. Crie lembretes, reporte bugs ou salve ideias para o futuro do sistema.
              </p>
            </div>
            <ul className="space-y-3">
              <li className="flex items-center gap-3 text-sm text-slate-300">
                <CheckCircle2 size={16} className="text-green-500" /> Post-its virtuais
              </li>
              <li className="flex items-center gap-3 text-sm text-slate-300">
                <CheckCircle2 size={16} className="text-green-500" /> Categorias (Bug, Ideia, Lembrete)
              </li>
            </ul>
            <Link href="/notas" className="inline-flex items-center gap-2 text-amber-400 font-bold hover:text-amber-300 transition-colors">
              Ver Mural <ArrowRight size={16} />
            </Link>
          </div>
        </div>

      </div>

      {/* Footer (Decorativo) */}
      <div className="flex items-center justify-center gap-2 text-slate-500 text-sm py-8 opacity-60">
        <ShieldCheck size={16} />
        <span>Ambiente Seguro • LexControl v1.0 • Desenvolvido para Alta Performance</span>
      </div>

    </div>
  );
}