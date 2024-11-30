import React from 'react';
import NavbarAdmin from '../components/NavbarAdmin'; 
import Footer from '../components/Footer'; 
import Bedel from '../components/Bedel'; 

function BedelPage() {
  return (
    <>
      <NavbarAdmin />
      <div className="container">
        <Bedel />
      </div>
      <Footer />
    </>
  );
}

export default BedelPage;
