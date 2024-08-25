import React, { useState } from 'react';
import axios from 'axios';

const SearchClients = () => {
    const [searchParams, setSearchParams] = useState({ name: '', phone: '', email: '' });
    const [results, setResults] = useState([]);

    const handleChange = (e) => {
        setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.get('/api/search', { params: searchParams });
            setResults(response.data);
        } catch (error) {
            console.error('There was an error searching clients!', error);
        }
    }

    return (
        <div>
            <h2>Search Clients</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Name</label>
                    <input type="text" className="form-control" name="name" onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label>Phone</label>
                    <input type="text" className="form-control" name="phone" onChange={handleChange} />
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" className="form-control" name="email" onChange={handleChange} />
                </div>
                <button type="submit" className="btn btn-primary">Search</button>
            </form>
            <div>
                <h3>Results</h3>
                <ul>
                    {results.map(client => (
                        <li key={client.id}>{client.name} - {client.email}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default SearchClients;
