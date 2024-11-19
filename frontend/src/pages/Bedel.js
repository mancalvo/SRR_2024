import React, { useState, useEffect } from 'react';
import axios from 'axios'; 
import BedelModal from '../components/BedelModal';

function Bedel() {
  const [mostrarModal, setMostrarModal] = useState(false);
  const [listaBedeles, setListaBedeles] = useState([]);
  const [editarBedel, setEditarBedel] = useState(null);

  useEffect(() => {
    // Cargar los bedeles desde el backend
    const obtenerBedeles = async () => {
      try {
        const respuesta = await axios.get('http://localhost:8080/usuarios/bedels'); 
        setListaBedeles(respuesta.data);
      } catch (error) {
        console.error('Error al cargar bedeles:', error);
      }
    };

    obtenerBedeles();
  }, []);

  const manejarMostrarModal = (bedel = null) => {
    setEditarBedel(bedel);
    setMostrarModal(true);
  };

  const manejarCerrarModal = () => {
    setMostrarModal(false);
    setEditarBedel(null);
  };

  const manejarGuardarBedel = (bedel) => {
    if (editarBedel) {
      // Actualizar el bedel existente
      setListaBedeles(prevLista => prevLista.map(b => (b.idUsuario === bedel.idUsuario ? bedel : b)));
    } else {
      // Agregar un nuevo bedel
      setListaBedeles(prevLista => [...prevLista, { ...bedel, idUsuario: Date.now() }]);
    }
    manejarCerrarModal();
  };

  const manejarEliminarBedel = (idUsuario) => {
    setListaBedeles(prevLista => prevLista.filter(bedel => bedel.idUsuario !== idUsuario));
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
            <button type="button" className="btn btn-outline-success" id="btnAgregar" onClick={() => manejarMostrarModal()}>
              Agregar
            </button>
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col">
          <div className="table-responsive">
            <table className="table table-hover table-bordered">
              <caption>Cantidad de Bedel: {listaBedeles.length}</caption>
              <thead>
                <tr className="table-dark text-center align-middle">
                  <th>Nombre Y Apellido</th>
                  <th>Turno</th>
                  <th style={{ width: '210px' }}>Acciones</th>
                </tr>
              </thead>

              <tbody className="text-center align-middle">
                {listaBedeles.map((bedel) => (
                  <tr key={bedel.id}>
                    <td>{bedel.nombre}</td>
                    <td>{bedel.turno}</td>
                    <td>
                      <div className="d-flex justify-content-evenly align-items-center">
                        <button type="button" className="btn btn-warning me-1" onClick={() => manejarMostrarModal(bedel)}>
                          Editar
                        </button>
                        <button type="button" className="btn btn-danger ms-1" onClick={() => manejarEliminarBedel(bedel.id)}>
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
      {mostrarModal && <BedelModal manejarCerrar={manejarCerrarModal} manejarGuardar={manejarGuardarBedel} bedel={editarBedel} />}
    </div>
  );
}

export default Bedel;
