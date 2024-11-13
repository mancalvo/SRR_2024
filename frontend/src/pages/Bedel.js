import React, { useState, useEffect } from 'react';
import BedelModal from '../components/BedelModal';

function Bedel() {
  const [showModal, setShowModal] = useState(false);
  const [bedelList, setBedelList] = useState([]);
  const [editBedel, setEditBedel] = useState(null); // Para editar un bedel

  useEffect(() => {
    // Datos simulados
    const simulatedData = [
      { id: 1, nombre: 'Dardo Sanchez', turno: 'MAÑANA' },
      { id: 2, nombre: 'Lionel Messi', turno: 'TARDE' },
      { id: 3, nombre: 'Cristiano Ronaldo', turno: 'NOCHE' },
    ];
    setBedelList(simulatedData);
  }, []);

  const handleShowModal = (bedel = null) => {
    setEditBedel(bedel);  // Si es un Bedel para editar, se carga aquí
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setEditBedel(null);  // Limpiar el bedel a editar al cerrar el modal
  };

  const handleSaveBedel = (bedel) => {
    if (editBedel) {
      // Actualizar el bedel existente
      setBedelList(prevList => prevList.map(b => b.id === bedel.id ? bedel : b));
    } else {
      // Agregar un nuevo bedel
      setBedelList(prevList => [...prevList, { ...bedel, id: Date.now() }]); // Asignamos un ID único
    }
    handleCloseModal();  // Cerrar el modal después de guardar
  };

  const handleDeleteBedel = (id) => {
    setBedelList(prevList => prevList.filter(bedel => bedel.id !== id));
  };

  return (
    <div className="container">
      <div className="row mt-4 justify-content-center">
        <div className="col-6 text-center">
          <h2>ADMINISTRACION DE BEDEL</h2>
        </div>
      </div>

      <div className="row">
        <div className="col-6 d-flex justify-content-start align-items-center">
          <input className="form-control w-50 me-2 w-auto" type="text" placeholder="Buscar" />
          <button type="button" className="btn btn-outline-dark ms-2" id="btnBuscar">
            Buscar
          </button>
        </div>
        <div className="col-6">
          <div className="d-flex justify-content-end">
            <button type="button" className="btn btn-outline-success" id="btnAgregar" onClick={() => handleShowModal()}>
              Agregar
            </button>
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col">
          <div className="table-responsive">
            <table className="table table-hover table-bordered">
              <caption>Cantidad de Bedel: {bedelList.length}</caption>
              <thead>
                <tr className="table-dark text-center align-middle">
                  <th>Nombre Y Apellido</th>
                  <th>Turno</th>
                  <th style={{ width: '210px' }}>Acciones</th>
                </tr>
              </thead>

              <tbody className="text-center align-middle">
                {bedelList.map((bedel) => (
                  <tr key={bedel.id}>
                    <td>{bedel.nombre}</td>
                    <td>{bedel.turno}</td>
                    <td>
                      <div className="d-flex justify-content-evenly align-items-center">
                        <button type="button" className="btn btn-warning me-1" onClick={() => handleShowModal(bedel)}>
                          Editar
                        </button>
                        <button type="button" className="btn btn-danger ms-1" onClick={() => handleDeleteBedel(bedel.id)}>
                          Eliminar
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Renderizar el Modal */}
      {showModal && <BedelModal handleClose={handleCloseModal} handleSave={handleSaveBedel} bedel={editBedel} />}
    </div>
  );
}

export default Bedel;
