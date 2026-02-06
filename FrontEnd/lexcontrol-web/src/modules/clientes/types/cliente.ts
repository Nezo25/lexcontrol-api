import { Endereco } from '@/shared/types/endereco';

export interface Cliente {
  id?: number;
  nomeCliente: string;
  cpf: string;
  rg: string;
  causa: string;

  valorCausa: number;
  statusPagamento: 'PENDENTE' | 'PAGO' | 'ATRASADO';

  dataVencimento: string;

  modeloDePagamento: 'AVISTA' | 'PARCELADO';
  valorParcela?: number;
  totalHonorarios: number;

  endereco?: Endereco;
}
