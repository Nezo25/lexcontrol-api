import { Nota } from "../types/notas";

const MOCK_NOTAS: Nota[] = [
  { 
    id: 1, 
    titulo: 'Endpoint Escritório', 
    conteudo: 'Implementar o endpoint novo de escritório e remover os dados fixos da tela de lembrete.', 
    data: '2026-02-06', 
    tipo: 'LEMBRETE' 
  },
  { 
    id: 2, 
    titulo: 'Java Local', 
    conteudo: 'Configurar o Java para rodar localmente e corrigir erros de conexão.', 
    data: '2026-02-05', 
    tipo: 'BUG' 
  },
  { 
    id: 3, 
    titulo: 'Animações JSON', 
    conteudo: 'Adicionar animações vivas via JSON com libs livres da internet (Lottie) para melhorar a UX.', 
    data: '2026-02-06', 
    tipo: 'IDEIA' 
  },
];

export const notaService = {
  listar: async (): Promise<Nota[]> => {
    return new Promise((resolve) => setTimeout(() => resolve(MOCK_NOTAS), 500));
  }
};