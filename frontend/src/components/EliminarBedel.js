import axios from "axios";
import React, { useState, useEffect } from "react";

const EliminarBedel = ({ idUsuario, abrirModal, cerrarModal,actualizarBedeles }) => {
  const [bedel, setBedel] = useState({
    nombre: "",
    apellido: "",
    tipoTurno: "",
  }); 

  useEffect(() => {
    const obtenerBedel = async () => {
      if (idUsuario) {
        try {
          const response = await axios.get(
            `http://localhost:8080/usuarios/bedel/${idUsuario}`
          );
          setBedel(response.data);
        } catch (error) {
          console.error("Error al obtener el bedel:", error);
        }
      }
    };

    obtenerBedel();
  }, [idUsuario]);

  const handleEliminar = async () => {
    try {
      const response = await axios.delete(
        `http://localhost:8080/usuarios/bedel/${idUsuario}`
      );
      if (response.status === 200) {
        alert(`Bedel con Id ${idUsuario} eliminado correctamente`);
        actualizarBedeles(); 
        cerrarModal();
      }
    } catch (error) {
      console.error("Error al eliminar el bedel:", error);
      alert("Hubo un error al eliminar el bedel.");
    }
  };
  

  return (
    <div className="modal show d-block" tabIndex="-1">
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">Esta seguro que desea eliminar?</h5>
            <button
              type="button"
              className="btn-close"
              onClick={cerrarModal} 
              aria-label="Close"
            ></button>
          </div>
          <div className="modal-body">
            <p>
              <strong>Nombre:</strong> {bedel.nombre || "No disponible"}
            </p>
            <p>
              <strong>Apellido:</strong> {bedel.apellido || "No disponible"}
            </p>
            <p>
              <strong>Turno:</strong> {bedel.tipoTurno || "No disponible"}
            </p>
            <div className="d-flex justify-content-center mt-3">
              <button
                type="button"
                className="btn btn-light me-2"
                onClick={cerrarModal} 
              >
                Cancelar
              </button>
              <button
                type="button"
                className="btn btn-danger"
                onClick={handleEliminar} 
              >
                Confirmar Eliminación
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EliminarBedel;
