import React, { useState, useEffect } from "react";
import './Counters.css';

const EditCounters = () => {
  const [officesWithCounters, setOfficesWithCounters] = useState([]);
  const [offices, setOffices] = useState([]);
  const [newCounterName, setNewCounterName] = useState("");
  const [selectedOfficeId, setSelectedOfficeId] = useState("");

  useEffect(() => {
    fetchOfficesWithCounters();
    fetchOffices();
  }, []);

  const fetchOfficesWithCounters = async () => {
    const res = await fetch("http://localhost:8080/offices");
    const data = await res.json();
    setOfficesWithCounters(data);
  };

  const fetchOffices = async () => {
    const res = await fetch("http://localhost:8080/offices");
    const data = await res.json();
    setOffices(data);
  };

  const addCounter = async () => {
    if (!selectedOfficeId || !newCounterName) {
      alert("Please select an office and enter the counter name!");
      return;
    }

    await fetch(`http://localhost:8080/counters/${selectedOfficeId}`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name: newCounterName }),
    });

    setNewCounterName("");
    setSelectedOfficeId("");
    fetchOfficesWithCounters();
  };

  // Delete a counter
  const deleteCounter = async (counterId) => {
    await fetch(`http://localhost:8080/counters/${counterId}`, {
      method: "DELETE",
    });

    fetchOfficesWithCounters();
  };

  return (
    <div className={'counters-container'}>
      <h1>Edit Counters</h1>

      <div>
        <input
          type="text"
          placeholder="New Counter Name"
          value={newCounterName}
          onChange={(e) => setNewCounterName(e.target.value)}
        />

        <select
          value={selectedOfficeId}
          onChange={(e) => setSelectedOfficeId(e.target.value)}
        >
          <option value="">Select Office</option>
          {offices.map((office) => (
            <option key={office.id} value={office.id}>
              {office.name} (ID: {office.id})
            </option>
          ))}
        </select>

        <button onClick={addCounter}>Add Counter</button>
      </div>

      <div>
        <h2>Available Counters by Office</h2>
        {officesWithCounters.map((office) => (
          <div key={office.id}>
            <h3>
              {office.name} (ID: {office.id})
            </h3>
            <ul>
              {office.counters && office.counters.length > 0 ? (
                office.counters.map((counter) => (
                  <li key={counter.id}>
                    {counter.name}{" "}
                    <button onClick={() => deleteCounter(counter.id)}>Delete</button>
                  </li>
                ))
              ) : (
                <li>No counters available</li>
              )}
            </ul>
          </div>
        ))}
      </div>
    </div>
  );
};

export default EditCounters;
