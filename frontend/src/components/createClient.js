import React, { useState } from 'react';
import axios from 'axios';

const CreateClient = () => {
    const [client, setClient] = useState({ name: '', email: '', phone: '', password: '' });

    const handleChange = (e) => {
        setClient({ ...client, [e.target.name]: e.target.value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post('/api/create', client);
            alert('Client created successfully: ' + response.data.name);
        } catch (error) {
            console.error('There was an error creating the client!', error);
        }
    }

    return (
        <div>
            <h2>Create Client</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Name</label>
                    <input type="text" className="form-control" name="name" onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Email</label>
                    <input type="email" className="form-control" name="email" onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Phone</label>
                    <input type="text" className="form-control" name="phone" onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Password</label>
                    <input type="password" className="form-control" name="password" onChange={handleChange} required />
                </div>
                <button type="submit" className="btn btn-primary">Create Client</button>
            </form>
        </div>
    );
}

export default CreateClient;
