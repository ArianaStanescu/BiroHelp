import React, { useState } from 'react';
import { useAuth } from './AuthContext';
import './Settings.css';

function Settings() {
    const { authenticatedUser, setAuthenticatedUser } = useAuth();
    const [name, setName] = useState(authenticatedUser?.name || '');
    const [username, setUsername] = useState(authenticatedUser?.username || '');
    const [message, setMessage] = useState('');

    const handleUpdate = async () => {
        try {
            const response = await fetch(`http://localhost:8080/clients/${authenticatedUser.id}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id: authenticatedUser.id, name, username }),
            });

            if (response.ok) {
                const updatedUser = await response.json();
                setAuthenticatedUser(updatedUser);
                setMessage('Profile updated successfully!');
            } else {
                setMessage('Failed to update profile. Please try again.');
            }
        } catch (error) {
            setMessage('An error occurred. Please try again later.');
        }
    };

    return (
        <div className="settings-container">
            <h1>Settings</h1>
            <p>Your username is: {authenticatedUser?.username}</p>
            <label>
                Change Name:
                <input
                    type="text"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </label>
            <br />
            <label>
                Change Username:
                <input
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                />
            </label>
            <br />
            <button onClick={handleUpdate}>Save Changes</button>
            {message && <p className="message">{message}</p>}
        </div>
    );
}

export default Settings;
