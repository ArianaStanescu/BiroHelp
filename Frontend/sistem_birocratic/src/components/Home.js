import React, { useEffect, useState } from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./style.css";

function Home() {
    const [ownedDocuments, setOwnedDocuments] = useState([]);
    const { authenticatedUser } = useAuth();
    const navigate = useNavigate();

    const fetchUserDocuments = async () => {
        try {
            const response = await fetch(`http://localhost:8080/clients/${authenticatedUser.id}`);
            if (response.ok) {
                const data = await response.json();
                setOwnedDocuments(data.ownedDocuments || []);
            } else {
                console.error("Failed to fetch user documents.");
            }
        } catch (error) {
            console.error("Error fetching user documents:", error);
        }
    };

    useEffect(() => {
        if (authenticatedUser) {
            fetchUserDocuments();
        }
    }, [authenticatedUser]);

    if (!authenticatedUser) {
        return (
            <div className="welcome-container">
                <h1>Welcome to BiroHelp!</h1>
                <p>You need to log in to access your dashboard.</p>
                <button onClick={() => navigate("/login")}>Go to Login</button>
            </div>
        );
    }

    return (
        <div className="auth-container">
            <h1>Welcome, {authenticatedUser.name}!</h1>
            <p>Username: {authenticatedUser.username}</p>

            <div>
                <h2>Your Owned Documents</h2>
                {ownedDocuments.length > 0 ? (
                    <ul className="documents-list">
                        {ownedDocuments.map((doc) => (
                            <li key={doc.id} className="document-item">
                                <h3>{doc.name}</h3>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p className="no-documents">You currently don't own any documents.</p>
                )}
            </div>

            <button onClick={() => navigate("/request-documents")}>
                I want to request a document
            </button>
        </div>
    );
}

export default Home;
