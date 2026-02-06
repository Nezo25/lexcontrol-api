import { api } from "@/shared/services/api";
import { Cliente } from "../types/cliente";

export const clienteService = {
  criar: async (cliente: Cliente) => {
    const response = await api.post('/clientes', cliente);
    return response.data;
  },

  listar: async (): Promise<Cliente[]> => {
    const response = await api.get('/clientes');
    return response.data;
  }
};