import React from "react";
import { useAuth } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import "./style.css";

function Home() {
    const { authenticatedUser } = useAuth();
    const navigate = useNavigate();

    if (!authenticatedUser) {
        return (
            <div className="welcome-container">
                <h1>Hi, admin!</h1>
                <p>You need to log in to access your dashboard.</p>
                <button onClick={() => navigate("/login")}>Go to Login</button>
            </div>
        );
    }

    return (
        <div className="auth-container">
            <h1>Welcome, Admin!</h1>
            <p>Please choose an action:</p>
            <div className="button-group">
                <button onClick={() => navigate("/documents")}>Edit Documents</button>
                <button onClick={() => navigate("/offices")}>Edit Offices</button>
                <button onClick={() => navigate("/counters")}>Edit Counters</button>
                <button onClick={() => navigate("/clients")}>Edit Clients</button>
            </div>
        </div>
    );
}

export default Home;
