import { Wallet } from "lucide-react";
import { Cliente } from "@/types/cliente";

interface ClienteCardProps {
  cliente: Cliente;
}

export function ClienteCard({ cliente }: ClienteCardProps) {
  return (
    <div className="bg-white p-6 rounded-2xl shadow-sm border border-slate-100 flex justify-between items-center hover:border-blue-200 transition-colors">
      <div>
        <h3 className="font-semibold text-lg text-slate-800">{cliente.nomeCliente}</h3>
        <p className="text-sm text-slate-500">CPF: {cliente.cpf}</p>
        <span className="text-xs font-medium bg-slate-100 text-slate-600 px-2 py-1 rounded mt-2 inline-block">
          {cliente.causa}
        </span>
      </div>
      <div className="text-right">
        <div className="flex items-center justify-end gap-1 font-mono font-medium text-slate-700">
          <Wallet size={14} className="text-slate-400" />
          {cliente.valorCausa.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })}
        </div>
        <span className={`text-xs font-bold px-2 py-1 rounded mt-1 inline-block ${
          cliente.statusPagamento === 'PAGO' ? 'bg-green-100 text-green-700' : 'bg-orange-100 text-orange-700'
        }`}>
          {cliente.statusPagamento}
        </span>
      </div>
    </div>
  );
}