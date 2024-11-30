import React from 'react';
import { Link, useNavigate } from 'react-router-dom';  

function NavbarAdmin() {
  const navigate = useNavigate();  

  const handleLogout = () => {
    localStorage.removeItem('tipoUsuario'); 
    navigate('/login'); 
  };

  return (
    <nav className="navbar navbar-expand-lg bg-dark border-bottom border-body" data-bs-theme="dark">
      <div className="container">
        <Link className="navbar-brand" to="/">Navbar</Link>
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false">
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="container">
          <div className="collapse navbar-collapse" id="navbarNavDropdown">
            <ul className="navbar-nav">
              {/* Cambiar el 'to' a la ruta correcta '/bedel' */}
              <li className="nav-item">
                <Link className="nav-link" to="/bedel">Bedel</Link>
              </li>
              <li className="nav-item">
                <button className="nav-link btn" onClick={handleLogout}>Cerrar Sesion</button> 
              </li>
            </ul>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default NavbarAdmin;

