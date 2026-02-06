'use client';

import { useQuery } from '@tanstack/react-query';
import { StickyNote, Plus, Loader2 } from 'lucide-react';
import { notaService } from '@/modules/notas/services/notaService';
import { NotaCard } from '@/modules/notas/componets/NotaCard';

export default function NotasPage() {
  const { data: notas, isLoading } = useQuery({
    queryKey: ['notas'],
    queryFn: notaService.listar,
  });

  return (
    <div className="max-w-6xl mx-auto space-y-8">
      
      <header className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <div className="p-3 bg-amber-500/10 text-amber-500 rounded-xl border border-amber-500/20 shadow-[0_0_15px_rgba(245,158,11,0.1)]">
            <StickyNote size={24} />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-white tracking-tight">Quadro de Avisos</h1>
            <p className="text-slate-400 text-sm">Recados internos da equipe de Dev</p>
          </div>
        </div>
        
        <button className="bg-slate-800 hover:bg-slate-700 text-white px-4 py-2 rounded-lg font-medium transition-all border border-slate-700 flex items-center gap-2">
          <Plus size={18} />
          Novo Recado
        </button>
      </header>

      {/* Grid de Recados */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {isLoading && (
           <div className="col-span-full flex justify-center py-20">
             <Loader2 className="animate-spin text-amber-500" size={32} />
           </div>
        )}

        {notas?.map((nota) => (
          <NotaCard key={nota.id} nota={nota} />
        ))}
      </div>
    </div>
  );
}