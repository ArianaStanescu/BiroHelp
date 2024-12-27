import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css';

function Register() {
    const [name, setName] = useState('');
    const [username, setUsername] = useState('');
    const [ownedDocuments, setOwnedDocuments] = useState([]);
    const [availableDocuments, setAvailableDocuments] = useState([]);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    // Fetch available documents from the server
    useEffect(() => {
        const fetchDocuments = async () => {
            try {
                const response = await fetch('http://localhost:8080/documents');
                if (response.ok) {
                    const data = await response.json();
                    setAvailableDocuments(data); // Store available documents in state
                } else {
                    console.error('Failed to fetch available documents.');
                }
            } catch (error) {
                console.error('Error fetching available documents:', error);
            }
        };

        fetchDocuments();
    }, []);

    // Handle form submission to register the user
    const handleRegister = async (e) => {
        e.preventDefault();

        const clientData = {
            name,
            username,
            requestedDocumentIds: [], // You can add logic here if needed
            ownedDocumentsIds: ownedDocuments, // Only the selected (checked) documents
        };

        try {
            const response = await fetch('http://localhost:8080/clients', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(clientData),
            });

            if (response.ok) {
                setMessage('Registration successful! You can now log in.');
                setTimeout(() => navigate('/login'), 2000); // Redirect after 2 seconds
            } else {
                setMessage('Registration failed. Please try again.');
            }
        } catch (error) {
            setMessage('An error occurred. Please try again later.');
        }
    };

    // Handle the checkbox change for selecting/deselecting owned documents
    const handleDocumentChange = (e) => {
        const documentId = e.target.value;
        setOwnedDocuments((prevOwnedDocuments) =>
            prevOwnedDocuments.includes(documentId)
                ? prevOwnedDocuments.filter((id) => id !== documentId) // Remove if already selected
                : [...prevOwnedDocuments, documentId] // Add if not selected
        );
    };

    return (
        <div className="auth-container">
            <h1 className="title">Register</h1>
            <form onSubmit={handleRegister} className="register-form">
                <label className="form-label">
                    Name:
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                        className="input-field"
                    />
                </label>
                <label className="form-label">
                    Username:
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                        className="input-field"
                    />
                </label>
                <div className="form-label">
                    <p>Select Owned Documents:</p>
                    <div className="checkbox-group">
                        {availableDocuments.map((doc) => (
                            <div key={doc.id} className="checkbox-item">
                                <label className="checkbox-wrapper">
                                    <input
                                        type="checkbox"
                                        value={doc.id}
                                        onChange={handleDocumentChange}
                                        className="checkbox-input"

                                    />
                                    {doc.name} (Issued by: {doc.issuingOffice.name})
                                </label>
                            </div>
                        ))}
                    </div>
                </div>
                <button type="submit" className="submit-button">
                    Register
                </button>
            </form>
            {message && <p className="message">{message}</p>}
        </div>
    );
}

export default Register;
