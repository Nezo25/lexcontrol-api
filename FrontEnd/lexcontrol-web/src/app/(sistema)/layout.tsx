'use client';

import Link from 'next/link';
import { ChevronRight, Menu, X } from 'lucide-react'; // Importamos Menu e X
import { usePathname } from 'next/navigation';
import { APP_ROUTES } from '@/config/app-routes';
import { useUser } from '@/shared/context/UserContext';
import { WelcomeScreen } from '@/modules/clientes/components/WelcomeScreen';
import { useState, useEffect } from 'react';

export default function SistemaLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname();
  const { userName, isLoaded } = useUser();
  
  // Controle do menu mobile
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  // Fecha o menu mobile automaticamente ao trocar de rota
  useEffect(() => {
    setIsMobileMenuOpen(false);
  }, [pathname]);
  
  if (!isLoaded) return null; 
  
  if (!userName) {
    return (
      <div className="min-h-screen bg-slate-950 flex items-center justify-center p-4">
        <WelcomeScreen />
      </div>
    );
  }

  return (
    <div className="flex min-h-screen bg-slate-950 text-slate-100 relative">

      {/* --- BARRA SUPERIOR MOBILE --- */}
      <div className="md:hidden fixed top-0 left-0 w-full h-16 bg-slate-900/80 backdrop-blur-xl border-b border-slate-800 z-40 flex items-center justify-between px-4">
        <h1 className="text-xl font-bold text-white tracking-tight flex items-center gap-2">
          LexControl <span>⚖️</span>
        </h1>
        <button 
          onClick={() => setIsMobileMenuOpen(true)}
          className="p-2 bg-slate-800 rounded-lg text-slate-400 hover:text-white transition-colors"
        >
          <Menu size={24} />
        </button>
      </div>

      {/* --- BACKDROP MOBILE --- */}
      {/* Fundo escuro quando o menu tá aberto no celular */}
      {isMobileMenuOpen && (
        <div 
          className="md:hidden fixed inset-0 bg-black/60 backdrop-blur-sm z-40"
          onClick={() => setIsMobileMenuOpen(false)}
        />
      )}

      {/* --- SIDEBAR DINÂMICA --- */}
      <aside className={`
        fixed top-0 left-0 h-full w-64 bg-slate-900/95 backdrop-blur-xl border-r border-slate-800 flex flex-col z-50 transition-transform duration-300 ease-in-out
        ${isMobileMenuOpen ? 'translate-x-0' : '-translate-x-full'} 
        md:translate-x-0 md:bg-slate-900/50
      `}>
        
        {/* Header da Sidebar (Logo) */}
        <div className="p-6 md:p-8 border-b border-slate-800/50 flex items-center justify-between">
          <div>
            <div className="flex items-center gap-2 text-blue-500 mb-1">
              <div className="w-2 h-2 rounded-full bg-blue-500 animate-pulse" />
              <span className="text-xs font-bold tracking-widest uppercase">Online</span>
            </div>
            <h1 className="text-2xl font-bold text-white tracking-tight flex items-center gap-2">
              LexControl <span className="text-2xl">⚖️</span>
            </h1>
          </div>
          
          {/* Botão de fechar (Só no mobile) */}
          <button 
            className="md:hidden p-2 text-slate-400 hover:text-white hover:bg-slate-800 rounded-lg"
            onClick={() => setIsMobileMenuOpen(false)}
          >
            <X size={24} />
          </button>
        </div>

        {/* Navegação */}
        <nav className="flex-1 p-4 space-y-2 mt-2 md:mt-4 overflow-y-auto">
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

        {/* Footer da Sidebar */}
        <div className="p-6 border-t border-slate-800/50">
          <div className="bg-slate-800/50 rounded-lg p-4 text-center border border-slate-700/50">
            <p className="text-xs text-slate-500 font-medium mb-1">Logado como</p>
            <p className="text-sm text-slate-200 font-bold truncate">
              {userName}
            </p>
          </div>
        </div>
      </aside>

      {/* --- CONTEÚDO PRINCIPAL --- */}
      {/* ml-0 no mobile, md:ml-64 no desktop. pt-16 no mobile para não sumir atrás da barra superior */}
      <main className="flex-1 w-full md:ml-64 relative overflow-x-hidden pt-16 md:pt-0">
        <div className="absolute top-0 left-0 w-full h-96 bg-blue-900/10 blur-[120px] -z-10 rounded-full pointer-events-none" />
        
        <div className="p-4 sm:p-8 md:p-12 overflow-y-auto min-h-full">
          {children}
        </div>
      </main>
    </div>
  );
}