import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';

const ClientDetail = () => {
    const { clientId } = useParams(); // Get clientId from URL parameters
    const [client, setClient] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchClientDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/clients/${clientId}`);
                setClient(response.data);
                setLoading(false);
            } catch (err) {
                setError(err.message);
                setLoading(false);
            }
        };

        fetchClientDetails();
    }, [clientId]);

    if (loading) return <p>Loading...</p>;
    if (error) return <p>Error: {error}</p>;

    return (
        <div className="container mt-4">
            {client ? (
                <div>
                    <h2>Client Details</h2>
                    <dl className="row">
                        <dt className="col-sm-3">ID</dt>
                        <dd className="col-sm-9">{client.id}</dd>

                        <dt className="col-sm-3">Name</dt>
                        <dd className="col-sm-9">{client.name}</dd>

                        <dt className="col-sm-3">Email</dt>
                        <dd className="col-sm-9">{client.email}</dd>

                        <dt className="col-sm-3">Phone</dt>
                        <dd className="col-sm-9">{client.phone}</dd>

                        <dt className="col-sm-3">Date of Birth</dt>
                        <dd className="col-sm-9">{new Date(client.dateOfBirth).toLocaleDateString()}</dd>

                        <dt className="col-sm-3">Account Balance</dt>
                        <dd className="col-sm-9">{client.account?.balance || 'N/A'}</dd>
                    </dl>
                </div>
            ) : (
                <p>No client details found.</p>
            )}
        </div>
    );
};

export default ClientDetail;
