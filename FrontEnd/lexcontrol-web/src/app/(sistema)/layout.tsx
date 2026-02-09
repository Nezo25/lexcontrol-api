'use client';

import Link from 'next/link';
import { ChevronRight } from 'lucide-react';
import { usePathname } from 'next/navigation';
import { APP_ROUTES } from '@/config/app-routes';
import { useUser } from '@/shared/context/UserContext';
import { WelcomeScreen } from '@/modules/clientes/components/WelcomeScreen'; // Importação corrigida

export default function SistemaLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { userName, isLoaded } = useUser();
  
  // 1. Evita piscar a tela enquanto carrega o LocalStorage
  if (!isLoaded) return null; // Poderia ser um <Loading /> aqui se quisesse
  
  // 2. CENÁRIO A: Não tem nome -> Mostra Tela de Boas-Vindas (SEM Sidebar)
  if (!userName) {
    return (
      <div className="min-h-screen bg-slate-950 flex items-center justify-center p-4">
        <WelcomeScreen />
      </div>
    );
  }

  // 3. CENÁRIO B: Tem nome -> Mostra o Layout Completo (Sidebar + Conteúdo)
  return (
    <div className="flex min-h-screen bg-slate-950 text-slate-100">

      {/* SIDEBAR DINÂMICA */}
      <aside className="w-64 bg-slate-900/50 backdrop-blur-xl border-r border-slate-800 flex flex-col fixed h-full z-20 transition-all">
        
        {/* Header da Sidebar (Logo) */}
        <div className="p-8 border-b border-slate-800/50">
          <div className="flex items-center gap-2 text-blue-500 mb-1">
            <div className="w-2 h-2 rounded-full bg-blue-500 animate-pulse" />
            <span className="text-xs font-bold tracking-widest uppercase">Online</span>
          </div>
          {/* MUDANÇA: Logo com Emoji */}
          <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
            LexControl <span className="text-2xl">⚖️</span>
          </h1>
        </div>

        {/* Navegação Gerada Automaticamente */}
        <nav className="flex-1 p-4 space-y-2 mt-4">
          {APP_ROUTES.map((route) => {
            const isActive = route.path === '/' 
              ? pathname === '/' 
              : pathname.startsWith(route.path);
            
            return (
              <Link 
                key={route.path} 
                href={route.path}
                className={`group flex items-center justify-between p-3 rounded-xl transition-all duration-300 ${isActive
                  ? 'bg-blue-600/10 text-blue-400 border border-blue-600/20'
                  : 'text-slate-400 hover:bg-slate-800 hover:text-slate-200'
                }`}
              >
                <div className="flex items-center gap-3">
                  <route.icon size={20} className={isActive ? "text-blue-400" : "text-slate-500 group-hover:text-slate-300"} />
                  <span className="font-medium">{route.label}</span>
                </div>
                {isActive && <ChevronRight size={16} />}
              </Link>
            )
          })}
        </nav>

        {/* Footer da Sidebar (User Profile DINÂMICO) */}
        <div className="p-6 border-t border-slate-800/50">
          <div className="bg-slate-800/50 rounded-lg p-4 text-center border border-slate-700/50">
            <p className="text-xs text-slate-500 font-medium mb-1">Logado como</p>
            
            <p className="text-sm text-slate-200 font-bold truncate">
               Dr. {userName}
            </p>
          </div>
        </div>
      </aside>

      {/* Conteúdo Principal */}
      <main className="flex-1 ml-64 relative overflow-hidden">
        {/* Efeito de luz de fundo */}
        <div className="absolute top-0 left-0 w-full h-96 bg-blue-900/10 blur-[120px] -z-10 rounded-full pointer-events-none" />
        
        <div className="p-8 md:p-12 overflow-y-auto h-full">
          {children}
        </div>
      </main>
    </div>
  );
}