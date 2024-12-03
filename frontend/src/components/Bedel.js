import React, { useState, useEffect } from "react";
import axios from "axios";
import BedelModal from "./BedelModal";

function Bedel() {
  const [mostrarModal, setMostrarModal] = useState(false);
  const [listaBedeles, setListaBedeles] = useState([]);
  const [editarBedel, setEditarBedel] = useState(null);
  const [busqueda, setBusqueda] = useState(""); // Estado para almacenar el término de búsqueda

  const obtenerBedeles = async () => {
    try {
      const respuesta = await axios.get(
        "http://localhost:8080/usuarios/bedels"
      );
      setListaBedeles(respuesta.data);
    } catch (error) {
      console.error("Error:", error);
      alert("Error al cargar bedeles.");
    }
  };

  useEffect(() => {
    obtenerBedeles();
  }, []);

  // Función para mostrar el modal, pasando el bedel a editar si es necesario
  const manejarMostrarModal = (bedel = null) => {
    setEditarBedel(bedel);
    setMostrarModal(true);
  };

  // Función para cerrar el modal y cargar nuevamente la lista de bedeles
  const manejarCerrarModal = () => {
    setMostrarModal(false);
    setEditarBedel(null);
    obtenerBedeles(); // Vuelve a cargar los bedeles
  };

  // Función para eliminar un bedel 
  const manejarEliminarBedel = async (idUsuario) => {
    try {
      const respuesta = await axios.delete(
        `http://localhost:8080/usuarios/bedel/${idUsuario}`
      );

      alert(respuesta.data);

      // Después de eliminarlo, actualizamos la lista de bedeles
      setListaBedeles((prevLista) =>
        prevLista.filter((bedel) => bedel.idUsuario !== idUsuario)
      );
    } catch (error) {
      console.error("Error al eliminar el bedel:", error);
      alert("Error al eliminar el bedel.");
    }
  };

  // Filtrar la lista de bedeles según el término de búsqueda
  const bedelesFiltrados = listaBedeles.filter((bedel) => {
    const nombreCompleto = `${bedel.nombre} ${bedel.apellido}`.toLowerCase();
    return nombreCompleto.includes(busqueda.toLowerCase());
  });

  return (
    <div className="container">
      <div className="row mt-4 justify-content-center">
        <div className="col-6 text-center">
          <h2>ADMINISTRACION DE BEDEL</h2>
        </div>
      </div>

      <div className="row">
        <div className="col-6 d-flex justify-content-start align-items-center">
          <input
            className="form-control w-50 me-2 w-auto"
            type="text"
            placeholder="Buscar"
            value={busqueda}
            onChange={(e) => setBusqueda(e.target.value)} // Actualizamos el estado con el valor del input
          />
          <button
            type="button"
            className="btn btn-outline-dark ms-2"
            id="btnBuscar"
          >
            Buscar
          </button>
        </div>
        <div className="col-6">
          <div className="d-flex justify-content-end">
            <button
              type="button"
              className="btn btn-outline-success"
              id="btnAgregar"
              onClick={() => manejarMostrarModal()}
            >
              Agregar
            </button>
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col">
          <div className="table-responsive">
            <table className="table table-hover table-bordered">
              <caption>Cantidad de Bedel: {bedelesFiltrados.length}</caption>
              <thead>
                <tr className="table-dark text-center align-middle">
                  <th>Nombre Y Apellido</th>
                  <th>Turno</th>
                  <th>Nombre de Usuario</th>
                  <th style={{ width: "210px" }}>Acciones</th>
                </tr>
              </thead>

              <tbody className="text-center align-middle">
                {bedelesFiltrados.map((bedel) => (
                  <tr key={bedel.idUsuario}>
                    <td>{`${bedel.nombre} ${bedel.apellido}`}</td>
                    <td>{bedel.tipoTurno}</td>
                    <td>{bedel.nombreUsuario}</td>
                    <td>
                      <div className="d-flex justify-content-evenly align-items-center">
                        <button
                          type="button"
                          className="btn btn-warning me-1"
                          onClick={() => manejarMostrarModal(bedel)}
                        >
                          Editar
                        </button>
                        <button
                          type="button"
                          className="btn btn-danger ms-1"
                          onClick={() =>
                            manejarEliminarBedel(bedel.idUsuario)
                          }
                        >
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
      {mostrarModal && (
        <BedelModal
          cerrar={manejarCerrarModal}
          bedel={editarBedel}
          actualizarBedeles={obtenerBedeles} // Pasamos la función para actualizar los bedeles
        />
      )}
    </div>
  );
}

export default Bedel;
