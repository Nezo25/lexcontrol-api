import { Endereco } from '@/shared/types/endereco';


export interface Cliente {
  id?: number;
  nomeCliente: string;
  cpf: string;
  rg: string;
  telefone: string;
  dataDeVencimento: string;
  causa: string;
  statusPagamento: 'PENDENTE' | 'PAGO' | 'ATRASADO' | 'A_TRASADO';
  modeloDePagamento: 'A_VISTA' | 'PARCELADO';
  valorCausa: number;
  valorParcela: number;
  totalHonorarios: number;
  endereco?: Endereco;
}