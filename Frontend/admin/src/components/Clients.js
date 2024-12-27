import React, { useState, useEffect } from "react";
import "./Clients.css";

const EditClients = () => {
  const [clients, setClients] = useState([]);
  const [editedClients, setEditedClients] = useState({});

  useEffect(() => {
    fetchClients();
  }, []);

  const fetchClients = async () => {
    const res = await fetch("http://localhost:8080/clients");
    const data = await res.json();
    setClients(data);
  };

  const deleteClient = async (id) => {
    await fetch(`http://localhost:8080/clients/${id}`, {
      method: "DELETE",
    });
    fetchClients();
  };

  const updateClient = async (id) => {
    const updates = editedClients[id];
    if (!updates || (!updates.name && !updates.username)) {
      alert("Please provide a new name or username!");
      return;
    }

    const body = { id };
    if (updates.name) body.name = updates.name;
    if (updates.username) body.username = updates.username;

    await fetch(`http://localhost:8080/clients/${id}`, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });

    setEditedClients({ ...editedClients, [id]: { name: "", username: "" } });
    fetchClients();
  };

  const handleInputChange = (id, field, value) => {
    setEditedClients({
      ...editedClients,
      [id]: { ...editedClients[id], [field]: value },
    });
  };

  return (
      <div className="clients-container">
        <h1>Edit Clients</h1>
        <ul>
          {clients.map((client) => (
              <li key={client.id} className="client-item">
                <div className="client-container">
                  <h2>
                    Name: {client.name} -- Username: {client.username}
                  </h2>
                  <div>
                    <input
                        type="text"
                        placeholder="New Name"
                        value={editedClients[client.id]?.name || ""}
                        onChange={(e) =>
                            handleInputChange(client.id, "name", e.target.value)
                        }
                    />
                    <input
                        type="text"
                        placeholder="New Username"
                        value={editedClients[client.id]?.username || ""}
                        onChange={(e) =>
                            handleInputChange(client.id, "username", e.target.value)
                        }
                    />
                    <button
                        className="save-button"
                        onClick={() => updateClient(client.id)}
                    >
                      Save Changes
                    </button>
                    <button
                        className="delete-button"
                        onClick={() => deleteClient(client.id)}
                    >
                      Delete
                    </button>
                  </div>

                  {client.ownedDocuments && client.ownedDocuments.length > 0 && (
                      <div className="owned-documents">
                        <h3>Owned Documents:</h3>
                        <ul >
                          {client.ownedDocuments.map((doc) => (
                              <li key={doc.id} >
                                <h4>{doc.name}</h4>
                              </li>
                          ))}
                        </ul>
                      </div>
                  )}
                </div>
              </li>
          ))}
        </ul>
      </div>
  );
};

export default EditClients;
