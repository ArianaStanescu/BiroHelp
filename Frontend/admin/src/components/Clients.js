import React, { useState, useEffect } from "react";

const EditClients = () => {
  const [clients, setClients] = useState([]); // List of clients
  const [editedClients, setEditedClients] = useState({}); // Track updated fields per client

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
    <div>
      <h1>Edit Clients</h1>

      <ul>
        {clients.map((client) => (
          <li key={client.id}>
            <strong>
              Name - {client.name} :: Username - {client.username} : ID - {client.id}
            </strong>
            <div>
              {/* Input for Name */}
              <input
                type="text"
                placeholder="New Name"
                value={editedClients[client.id]?.name || ""}
                onChange={(e) =>
                  handleInputChange(client.id, "name", e.target.value)
                }
              />

              {/* Input for Username */}
              <input
                type="text"
                placeholder="New Username"
                value={editedClients[client.id]?.username || ""}
                onChange={(e) =>
                  handleInputChange(client.id, "username", e.target.value)
                }
              />

              {/* Buttons */}
              <button onClick={() => updateClient(client.id)}>Save Changes</button>
              <button onClick={() => deleteClient(client.id)}>Delete</button>
            </div>

            {/* Display Owned Documents */}
            {client.ownedDocuments && client.ownedDocuments.length > 0 && (
              <div>
                <h4>Owned Documents:</h4>
                <ul>
                  {client.ownedDocuments.map((doc) => (
                    <li key={doc.id}>
                      {doc.name} (ID: {doc.id})
                    </li>
                  ))}
                </ul>
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default EditClients;
