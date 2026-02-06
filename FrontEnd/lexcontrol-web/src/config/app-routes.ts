import { LayoutDashboard, Users, FileText, LucideIcon } from 'lucide-react';

export interface AppRoute {
  path: string;
  label: string;
  icon: LucideIcon;
  subItems?: AppRoute[]; // Preparado para submenus no futuro
}

// AQUI FICA A CONFIG DO menu lAteralll
export const APP_ROUTES: AppRoute[] = [
  { 
    path: '/', 
    label: 'Início', 
    icon: LayoutDashboard 
  },
  { 
    path: '/clientes', 
    label: 'Clientes', 
    icon: Users 
  },
  { 
    path: '/notas', 
    label: 'Notas', 
    icon: FileText 
  },
  // Futuro: { path: '/agenda', label: 'Agenda', icon: Calendar }
];