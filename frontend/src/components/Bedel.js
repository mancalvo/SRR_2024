import React, { useState, useEffect } from "react";
import axios from "axios";
import BedelModal from "./BedelModal";

function Bedel() {
  const [mostrarModal, setMostrarModal] = useState(false);
  const [listaBedeles, setListaBedeles] = useState([]);
  const [editarBedel, setEditarBedel] = useState(null);
  const [busqueda, setBusqueda] = useState("");
  const [paginaActual, setPaginaActual] = useState(1);
  const [bedelesPorPagina] = useState(5);
  
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

  const manejarMostrarModal = (bedel = null) => {
    setEditarBedel(bedel);
    setMostrarModal(true);
  };

  const manejarCerrarModal = () => {
    setMostrarModal(false);
    setEditarBedel(null);
    obtenerBedeles();
  };

  const manejarEliminarBedel = async (idUsuario, nombre, apellido) => {
    const confirmacion = window.confirm(`¿Estás seguro de que deseas borrar el bedel ${nombre} ${apellido}?`);
    
    if (!confirmacion) {
      // Si el usuario cancela, no hacemos nada
      return;
    }
  
    try {
      const respuesta = await axios.delete(
        `http://localhost:8080/usuarios/bedel/${idUsuario}`
      );
  
      alert(respuesta.data);
  
      setListaBedeles((prevLista) =>
        prevLista.filter((bedel) => bedel.idUsuario !== idUsuario)
      );
    } catch (error) {
      console.error("Error al eliminar el bedel:", error);
      alert("Error al eliminar el bedel.");
    }
  };

  const normalizarTexto = (texto) =>
    texto
      .toLowerCase()
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "");  // Sacamos los acentos
  
  const bedelesFiltrados = listaBedeles.filter((bedel) => {
    const nombreCompleto = normalizarTexto(`${bedel.nombre} ${bedel.apellido}`);
    const turno = normalizarTexto(bedel.tipoTurno);
    const busquedaNormalizada = normalizarTexto(busqueda);
    return (
      nombreCompleto.includes(busquedaNormalizada) ||
      turno.includes(busquedaNormalizada)
    );
  });
  
  

  const indiceInicio = (paginaActual - 1) * bedelesPorPagina;
  const indiceFin = indiceInicio + bedelesPorPagina;
  const bedelesPaginados = bedelesFiltrados.slice(indiceInicio, indiceFin);

  const totalPaginas = Math.ceil(bedelesFiltrados.length / bedelesPorPagina);

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
            onChange={(e) => setBusqueda(e.target.value)}
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
              <thead>
                <tr className="table-dark text-center align-middle">
                  <th>Nombre Y Apellido</th>
                  <th>Turno</th>
                  <th>Nombre de Usuario</th>
                  <th style={{ width: "210px" }}>Acciones</th>
                </tr>
              </thead>

              <tbody className="text-center align-middle">
                {bedelesPaginados.map((bedel) => (
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
                          onClick={() => manejarEliminarBedel(bedel.idUsuario, bedel.nombre, bedel.apellido)}
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

          <div className="d-flex justify-content-between">
            <span>Cantidad de Bedeles: {bedelesFiltrados.length}</span>

            <div>
              <button
                className="btn btn-outline-dark btn-sm me-2"
                disabled={paginaActual === 1}
                onClick={() => setPaginaActual(paginaActual - 1)}
              >
                &laquo;
              </button>
              <span>
                Página {paginaActual} de {totalPaginas}
              </span>
              <button
                className="btn btn-outline-dark btn-sm ms-2"
                disabled={paginaActual === totalPaginas}
                onClick={() => setPaginaActual(paginaActual + 1)}
              >
                &raquo;
              </button>
            </div>
          </div>
        </div>
      </div>

      {mostrarModal && (
        <BedelModal
          cerrar={manejarCerrarModal}
          bedel={editarBedel}
          actualizarBedeles={obtenerBedeles}
        />
      )}
    </div>
  );
}

export default Bedel;
