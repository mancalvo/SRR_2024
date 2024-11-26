import React from 'react';
import NavbarBedel from '../components/NavbarBedel'; // Asegúrate de tener el Navbar correspondiente
import Footer from '../components/Footer'; // Usamos el Footer común

function BedelPage() {
  return (
    <>
      <NavbarBedel />
      <div className="container">
        <h1>Bienvenido Bedel</h1>
      </div>
      <Footer />
    </>
  );
}

export default BedelPage;

