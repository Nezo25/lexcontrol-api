export interface Nota {
  id: number;
  titulo: string;
  conteudo: string;
  data: string;
  tipo: 'LEMBRETE' | 'BUG' | 'IDEIA';
}