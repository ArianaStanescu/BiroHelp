import React from "react";
import { useNavigate } from "react-router-dom";
import "./style.css";

function Finish() {
    const navigate = useNavigate();

    return (
        <div className="auth-container">
            <h1>Thank You!</h1>
            <p>Your document request has been submitted successfully.</p>
            <button onClick={() => navigate("/")}>
                Return to Home
            </button>
        </div>
    );
}

export default Finish;
