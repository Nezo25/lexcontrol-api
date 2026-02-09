'use client';

import { useState } from 'react';
import Lottie from 'lottie-react';
import { ArrowRight, Sparkles } from 'lucide-react';
import { useUser } from '@/shared/context/UserContext';

import welcomeAnimation from '../../../../public/animations/welcome.json'

export function WelcomeScreen() {
  const { setUserName } = useUser();
  const [inputName, setInputName] = useState('');

  const handleStart = (e: React.FormEvent) => {
    e.preventDefault();
    if (!inputName.trim()) return;
    setUserName(inputName.trim());
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-[60vh] text-center space-y-8 animate-in fade-in zoom-in duration-500">
      
      {/* Animação Lottie */}
      <div className="w-64 h-64 md:w-80 md:h-80">
        <Lottie animationData={welcomeAnimation} loop={true} />
      </div>

      <div className="max-w-md space-y-4">
        <h1 className="text-3xl md:text-4xl font-bold text-white tracking-tight">
          Bem-vindo ao LexControl <span className="text-blue-500">⚖️</span>
        </h1>
        <p className="text-slate-400 text-lg">
          Seu escritório inteligente começa aqui. <br/> Como podemos chamar você?
        </p>
      </div>

      <form onSubmit={handleStart} className="w-full max-w-sm relative group">
        <div className="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none">
          <Sparkles className="text-blue-500" size={20} />
        </div>
        <input 
          type="text" 
          value={inputName}
          onChange={(e) => setInputName(e.target.value)}
          placeholder="Digite seu nome..." 
          className="w-full pl-12 pr-4 py-4 bg-slate-900 border-2 border-slate-800 rounded-2xl text-white placeholder:text-slate-600 focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/20 transition-all text-lg"
          autoFocus
        />
        <button 
          type="submit"
          disabled={!inputName.trim()}
          className="absolute right-2 top-2 bottom-2 bg-blue-600 hover:bg-blue-500 text-white px-4 rounded-xl disabled:opacity-0 disabled:scale-90 transition-all duration-300 flex items-center gap-2 font-bold shadow-lg shadow-blue-900/50"
        >
          Entrar <ArrowRight size={18} />
        </button>
      </form>
    </div>
  );
}