import axios from 'axios';

// Instância do Axios configurada com o nosso backezao
export const api = axios.create({
  baseURL: 'http://localhost:8080', // Porta do Java ---> se nao for troca aqui
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    console.error('Erro na chamada API:', error?.response?.data || error.message);
    return Promise.reject(error);
  }
);