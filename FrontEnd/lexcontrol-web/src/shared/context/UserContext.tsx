'use client';

import { createContext, useContext, useState, useEffect } from 'react';

interface UserContextType {
    userName: string | null;
    setUserName: (name: string) => void;
    isLoaded: boolean; 
}

const UserContext = createContext<UserContextType>({} as UserContextType);

export function UserProvider({ children }: { children: React.ReactNode }) {
    const [userName, setUserNameState] = useState<string | null>(null);
    const [isLoaded, setIsLoaded] = useState(false);

    useEffect(() => {
        const savedName = localStorage.getItem('lexcontrol_user');
        if (savedName) {
            setUserNameState(savedName);
        }
        setIsLoaded(true);
    }, []);

    const setUserName = (name: string) => {
        localStorage.setItem('lexcontrol_user', name);
        setUserNameState(name);
    };

    return (
        <UserContext.Provider value={{ userName, setUserName, isLoaded }}>
            {children}
        </UserContext.Provider>
    );
}

export const useUser = () => useContext(UserContext);