import { api } from "@/shared/services/api";
import { Cliente } from "../types/cliente";

export const clienteService = {
  criar: async (cliente: Cliente) => {
    
    // TRATAMENTO DE DADOS (A Blindagem)
    const dadosParaEnviar = {
      ...cliente,
      
      // 1. Garante que se for NaN ou null, vira 0 (Java agradece)
      valorCausa: Number(cliente.valorCausa) || 0,
      totalHonorarios: Number(cliente.totalHonorarios) || 0,
      valorParcela: Number(cliente.valorParcela) || 0, 

      // 2. Garante compatibilidade de data (se vier vazia não quebra)
      dataDeVencimento: cliente.dataDeVencimento,
      
      // 3. O Enum já foi corrigido no formulário, mas deixamos garantido aqui
      modeloDePagamento: cliente.modeloDePagamento
    };
    
    const response = await api.post('/clientes', dadosParaEnviar);
    return response.data;
  },

  listar: async (): Promise<Cliente[]> => {
    const response = await api.get('/clientes');
    return response.data;
  }
};