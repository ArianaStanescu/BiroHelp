import React, { useState, useEffect } from "react";
import './Offices.css';
const Offices = () => {
  const [offices, setOffices] = useState([]);
  const [newOfficeName, setNewOfficeName] = useState("");

  useEffect(() => {
    fetchOffices();
  }, []);

  const fetchOffices = async () => {
    const res = await fetch("http://localhost:8080/offices");
    const data = await res.json();
    setOffices(data);
  };

  const addOffice = async () => {
    await fetch("http://localhost:8080/offices", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newOfficeName }),
    });
    fetchOffices();
  };

  const deleteOffice = async (id) => {
    await fetch(`http://localhost:8080/offices/${id}`, {
      method: "DELETE",
    });
    fetchOffices();
  };

  return (
      <div className="offices-container">
      <h1>Edit Offices</h1>
      <input
        type="text"
        placeholder="New Office Name"
        value={newOfficeName}
        onChange={(e) => setNewOfficeName(e.target.value)}
      />
      <button onClick={addOffice}>Add Office</button>

      <ul>
        {offices.map((office) => (
          <li key={office.id}>
            <h3>{office.name}{" "}</h3>
            <button className="del-button" onClick={() => deleteOffice(office.id)}>Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Offices;
