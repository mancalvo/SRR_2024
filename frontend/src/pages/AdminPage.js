import React from 'react';
import NavbarAdmin from '../components/NavbarAdmin'; // Asegúrate de tener el Navbar correspondiente
import Footer from '../components/Footer'; // Usamos el Footer común

function AdminPage() {
  return (
    <>
      <NavbarAdmin />
      <div className="container">
        <h1>Bienvenido Administrador</h1>
      </div>
      <Footer />
    </>
  );
}

export default AdminPage;
