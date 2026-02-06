import { AlertCircle, Lightbulb, Pin } from "lucide-react";
import { Nota } from "../types/notas";

export function NotaCard({ nota }: { nota: Nota }) {
  const typeConfig = {
    LEMBRETE: { color: 'bg-blue-500', icon: Pin, label: 'Lembrete' },
    BUG: { color: 'bg-red-500', icon: AlertCircle, label: 'Bug' },
    IDEIA: { color: 'bg-amber-500', icon: Lightbulb, label: 'Ideia' },
  };

  const config = typeConfig[nota.tipo];

  return (
    <div className="bg-slate-900/60 backdrop-blur-md p-6 rounded-2xl border border-slate-800 hover:border-slate-700 transition-all group relative overflow-hidden">
      
      {/* Faixa colorida lateral */}
      <div className={`absolute left-0 top-0 bottom-0 w-1 ${config.color}`} />

      <div className="flex justify-between items-start mb-3">
        <div className={`inline-flex items-center gap-1.5 px-2 py-1 rounded-md text-[10px] font-bold uppercase tracking-wider ${config.color} text-white bg-opacity-20`}>
          <config.icon size={12} />
          {config.label}
        </div>
        <span className="text-xs text-slate-500 font-mono">{nota.data}</span>
      </div>

      <h3 className="text-lg font-bold text-slate-100 mb-2 group-hover:text-blue-400 transition-colors">
        {nota.titulo}
      </h3>
      
      <p className="text-sm text-slate-400 leading-relaxed">
        {nota.conteudo}
      </p>
    </div>
  );
}