import React from 'react';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';  // Import Bootstrap CSS

const Header = () => {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <Link className="navbar-brand" to="/">Banking System</Link>
            <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarNav">
                <ul className="navbar-nav">
                    <li className="nav-item active">
                        <Link className="nav-link" to="/">Home <span className="sr-only">(current)</span></Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/createClient">Create Client</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/TransferMoney">Transfer Money</Link>
                    </li>
                    <li className="nav-item">
                        <Link className="nav-link" to="/SearchClients">Search Clients</Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default Header;
