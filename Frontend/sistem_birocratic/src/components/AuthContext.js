import React, { createContext, useState, useContext } from 'react';

const AuthContext = createContext();

export const useAuth = () => {
    return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
    const [authenticatedUser, setAuthenticatedUser] = useState(null);

    React.useEffect(() => {
        const storedUser = localStorage.getItem('authenticatedUser');
        if (storedUser) {
            setAuthenticatedUser(JSON.parse(storedUser));
        }
    }, []);

    const logout = () => {
        localStorage.removeItem('authenticatedUser');
        setAuthenticatedUser(null);
    };

    return (
        <AuthContext.Provider value={{ authenticatedUser, setAuthenticatedUser, logout }}>
            {children}
        </AuthContext.Provider>
    );
};
