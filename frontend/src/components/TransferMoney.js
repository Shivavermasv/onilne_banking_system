import React, { useState } from 'react';
import axios from 'axios';

const TransferMoney = () => {
    const [transferData, setTransferData] = useState({ toAccountId: '', amount: '' });

    const handleChange = (e) => {
        setTransferData({ ...transferData, [e.target.name]: e.target.value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/api/transfer', transferData);
            alert('Transfer successful');
        } catch (error) {
            console.error('There was an error transferring money!', error);
        }
    }

    return (
        <div>
            <h2>Transfer Money</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>To Account ID</label>
                    <input type="text" className="form-control" name="toAccountId" onChange={handleChange} required />
                </div>
                <div className="form-group">
                    <label>Amount</label>
                    <input type="number" className="form-control" name="amount" onChange={handleChange} required />
                </div>
                <button type="submit" className="btn btn-primary">Transfer</button>
            </form>
        </div>
    );
}

export default TransferMoney;
