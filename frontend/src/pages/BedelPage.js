import React from 'react';
import NavbarBedel from '../components/NavbarBedel'; 
import Footer from '../components/Footer'; 


function BedelPage() {
  return (
    <>
      <NavbarBedel />
      <div className="container text-center mt-5">
      <h1>Bienvenido Usuario</h1>
      <h5>Mensaje de Bienvenida ACA</h5>
      </div>
      <Footer />
    </>
  );
}

export default BedelPage;
