import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './AuthContext';
import './Login.css';

function Login() {
    const [username, setUsername] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();
    const { setAuthenticatedUser } = useAuth();

    useEffect(() => {
        const storedUser = localStorage.getItem('authenticatedUser');
        if (storedUser) {
            setAuthenticatedUser(JSON.parse(storedUser));
            navigate('/');
        }
    }, [navigate, setAuthenticatedUser]);

    const handleLogin = (e) => {
        e.preventDefault();

        if (username.trim().toLowerCase().includes('admin')) {
            // Crearea unui utilizator admin simulat
            const adminUser = { id: 1, name: 'Admin', username: username.trim() };

            // Setarea mesajului de bun venit și actualizarea utilizatorului autentificat
            setMessage(`Welcome, ${adminUser.name}!`);
            setAuthenticatedUser(adminUser);

            // Navigarea către pagina principală
            navigate('/');
        } else {
            // Setarea mesajului de eroare pentru acces restricționat
            setMessage('Access denied. Only admin users can log in.');
        }
    };

    return (
        <div className="auth-container">
            <h1>Login admin</h1>
            <form onSubmit={handleLogin}>
                <label>
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </label>
                <br />
                <button type="submit">Login</button>
            </form>
            {message && <p className="message">{message}</p>}
            <p>
                Admin page
            </p>
        </div>
    );
}

export default Login;
