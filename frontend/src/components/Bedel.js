import React, { useState, useEffect } from "react";
import axios from "axios";
import BedelModal from "./BedelModal";
import EliminarBedel from "./EliminarBedel";

function Bedel() {
  const [mostrarModal, setMostrarModal] = useState(false);
  const [listaBedeles, setListaBedeles] = useState([]);
  const [editarBedel, setEditarBedel] = useState(null);
  const [busquedaApellido, setBusquedaApellido] = useState("");
  const [busquedaTurno, setBusquedaTurno] = useState("");
  const [paginaActual, setPaginaActual] = useState(1);
  const [bedelesPorPagina] = useState(5);
  const [mostrarEliminarModal, setMostrarEliminarModal] = useState(false);
  const [bedelAEliminar, setBedelAEliminar] = useState(null);

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

  const buscarBedeles = async () => {
    try {
      console.log("Buscando con parámetros:", busquedaApellido, busquedaTurno);
      if (!busquedaApellido.trim() && !busquedaTurno.trim()) {
        obtenerBedeles(); // Si no hay filtros, obtenemos todos los bedeles
        return;
      }

      const params = {};
      if (busquedaApellido) params.apellido = busquedaApellido;
      if (busquedaTurno) params.tipoTurno = busquedaTurno;

      const respuesta = await axios.get(
        "http://localhost:8080/usuarios/bedels/AYT",
        { params }
      );

      console.log("Respuesta de la API:", respuesta.data); // Verifica la estructura de la respuesta
      setListaBedeles(respuesta.data); // Cambié de 'respuesta.data.bedeles' a 'respuesta.data'
    } catch (error) {
      console.error("Error al buscar bedeles:", error);
      alert(error.response?.data?.message || "Error al realizar la búsqueda.");
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
    setBusquedaApellido(""); // Limpiar el campo de búsqueda de apellido
    setBusquedaTurno(""); // Limpiar el campo de búsqueda de turno
    obtenerBedeles();
  };

  const manejarEliminarBedel = (idUsuario) => {
    setBedelAEliminar(idUsuario);
    setMostrarEliminarModal(true);
  };

  const indiceInicio = (paginaActual - 1) * bedelesPorPagina;
  const indiceFin = indiceInicio + bedelesPorPagina;
  const bedelesPaginados = listaBedeles.slice(indiceInicio, indiceFin);

  const totalPaginas = Math.ceil(listaBedeles.length / bedelesPorPagina);

  return (
    <div className="container">
      <div className="row mt-4 justify-content-center">
        <div className="col-6 text-center mb-3">
          <h2>ADMINISTRACION DE BEDELES</h2>
        </div>
      </div>

      <div className="row">
        <div className="col-6 d-flex justify-content-start align-items-center">
          <input
            className="form-control w-50 me-2"
            type="text"
            placeholder="Buscar por Apellido"
            value={busquedaApellido}
            onChange={(e) => setBusquedaApellido(e.target.value)}
          />
          <input
            className="form-control w-50 me-2"
            type="text"
            placeholder="Buscar por Turno"
            value={busquedaTurno}
            onChange={(e) => setBusquedaTurno(e.target.value.toUpperCase())}
          />
          <button
            type="button"
            className="btn btn-outline-dark ms-2"
            onClick={buscarBedeles}
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
                  <th>Nombre y Apellido</th>
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
                          onClick={() =>
                            manejarEliminarBedel(
                              bedel.idUsuario,
                              bedel.nombre,
                              bedel.apellido
                            )
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

          <div className="d-flex justify-content-between">
            <span>Cantidad de Bedeles: {listaBedeles.length}</span>

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
      {mostrarEliminarModal && (
        <EliminarBedel
          idUsuario={bedelAEliminar} 
          abrirModal={setMostrarEliminarModal}
          cerrarModal={() => setMostrarEliminarModal(false)}
          actualizarBedeles={obtenerBedeles} 
        />
      )}
    </div>
  );
}

export default Bedel;
