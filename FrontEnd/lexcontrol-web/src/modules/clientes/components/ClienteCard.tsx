import { Wallet, Calendar, Phone, User, Eye } from "lucide-react";
import { Cliente } from "../types/cliente";

interface ClienteCardProps {
  cliente: Cliente;
  onDoubleClick?: (cliente: Cliente) => void;
}

export function ClienteCard({ cliente, onDoubleClick }: ClienteCardProps) {
  
  const isPago = cliente.statusPagamento === 'PAGO';
  const statusColor = isPago 
    ? 'bg-emerald-500/10 text-emerald-400 border-emerald-500/20' 
    : 'bg-amber-500/10 text-amber-400 border-amber-500/20';

  return (
    <div 
      onDoubleClick={() => onDoubleClick && onDoubleClick(cliente)}
      className="group bg-slate-900/60 backdrop-blur-md p-6 rounded-2xl border border-slate-800 hover:border-blue-500/30 hover:bg-slate-900/80 transition-all duration-300 shadow-sm hover:shadow-blue-900/10 cursor-pointer relative"
    >
      {/* Dica visual (Olho) */}
      <div className="absolute top-4 right-4 opacity-0 group-hover:opacity-100 transition-opacity text-xs text-slate-500 flex items-center gap-1">
        <Eye size={14} /> Duplo clique para ver
      </div>

      <div className="flex flex-col md:flex-row justify-between md:items-center gap-4">
        
        {/* Lado Esquerdo: Identificação */}
        <div className="space-y-2">
            <div className="flex items-center gap-3">
                <h3 className="font-bold text-lg text-slate-100 group-hover:text-blue-400 transition-colors flex items-center gap-2">
                    <User size={18} className="text-slate-500" />
                    {cliente.nomeCliente}
                </h3>
                <span className="text-[10px] font-bold uppercase tracking-wider bg-slate-800 text-slate-400 px-2 py-0.5 rounded border border-slate-700">
                    {cliente.causa}
                </span>
            </div>
            
            <div className="flex flex-col sm:flex-row gap-2 sm:gap-6 text-sm text-slate-500 font-mono">
                <p className="flex items-center gap-2">
                    <span className="w-1.5 h-1.5 rounded-full bg-slate-600"></span>
                    CPF: {cliente.cpf}
                </p>
                {cliente.telefone && (
                    <p className="flex items-center gap-2">
                        <Phone size={12} className="text-blue-500/70" />
                        {cliente.telefone}
                    </p>
                )}
            </div>
        </div>

        {/* Lado Direito: Financeiro */}
        <div className="flex items-center gap-6 border-t md:border-t-0 border-slate-800/50 pt-4 md:pt-0 justify-between md:justify-end">
            <div className="text-right">
                <p className="text-xs text-slate-500 mb-0.5 flex items-center justify-end gap-1">
                   Valor da Causa <Wallet size={12} />
                </p>
                <div className="font-mono font-medium text-slate-300">
                    {Number(cliente.valorCausa || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}
                </div>
                
                <p className="text-[10px] text-slate-600 mt-1 flex items-center justify-end gap-1">
                    <Calendar size={10} /> 
                    {cliente.dataDeVencimento 
                        ? new Date(cliente.dataDeVencimento).toLocaleDateString('pt-BR', {timeZone: 'UTC'}) 
                        : '-'}
                </p>
            </div>

            <div className={`px-3 py-1.5 rounded-lg border text-xs font-bold flex items-center gap-2 ${statusColor}`}>
                <div className={`w-1.5 h-1.5 rounded-full ${isPago ? 'bg-emerald-400' : 'bg-amber-400'} animate-pulse`} />
                {cliente.statusPagamento}
            </div>
        </div>

      </div>
    </div>
  );
}