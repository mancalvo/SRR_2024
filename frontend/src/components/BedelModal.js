import React, { useState, useEffect } from "react";

const ModalBedel = ({ cerrar, bedel, actualizarBedeles }) => {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [nombreUsuario, setNombreUsuario] = useState("");
  const [turno, setTurno] = useState("");
  const [password, setPassword] = useState("");
  const [confirmarPassword, setConfirmarPassword] = useState("");
  const [activo, setActivo] = useState(true);

  useEffect(() => {
    if (bedel) {
      setNombre(bedel.nombre || "");
      setApellido(bedel.apellido || "");
      setNombreUsuario(bedel.nombreUsuario || "");
      setTurno(bedel.tipoTurno || "");
      setActivo(bedel.activo !== undefined ? bedel.activo : true);
      setPassword(bedel.contrasenia);
      setConfirmarPassword(bedel.contrasenia);
    }
  }, [bedel]);

  const guardarBedel = async (e) => {
    e.preventDefault();

    const nuevoBedel = {
      idUsuario: bedel?.idUsuario || null,
      nombre,
      apellido,
      nombreUsuario,
      tipoUsuario: "BEDEL",
      tipoTurno: turno,
      activo,
      contrasenia: password,
      repetirContrasenia: confirmarPassword,
    };

    try {
      const respuesta = bedel?.idUsuario
        ? await fetch(
            `http://localhost:8080/usuarios/bedel/${bedel.idUsuario}`,
            {
              method: "PUT",
              headers: {
                "Content-Type": "application/json",
              },
              body: JSON.stringify(nuevoBedel),
            }
          )
        : await fetch("http://localhost:8080/usuarios/crear", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify(nuevoBedel),
          });

      if (respuesta.ok) {
        const mensaje = await respuesta.text();
        alert(mensaje);

        // Llamar a actualizarBedeles para recargar la lista de bedeles después de guardar
        actualizarBedeles();

        cerrar();
      } else {
        const errorData = await respuesta.json();
        alert(errorData.message || "Error al guardar el usuario");
      }
    } catch (error) {
      console.error("Error:", error);
      alert("Hubo un problema al guardar el usuario");
    }
  };

  const manejarCancelar = () => {
    const mensajeConfirmacion = bedel
      ? "¿Estás seguro de que quieres cancelar? Los cambios no se guardarán."
      : "¿Estás seguro de que quieres cancelar? El bedel no se guardará.";
  
    const confirmacion = window.confirm(mensajeConfirmacion);
    
    if (confirmacion) {
      cerrar(); // Llama a la función 'cerrar' solo si el usuario confirma.
    }
  };
  
  

  const manejarCambioTurno = (e) => {
    const turnoMayuscula = e.target.value.toUpperCase();
    setTurno(turnoMayuscula);
  };

  return (
    <div className="modal show d-block" tabIndex="-1">
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">
              {bedel ? "Editar Bedel" : "Nuevo Bedel"}
            </h5>
            <button
              type="button"
              className="btn-close"
              onClick={cerrar}
              aria-label="Close"
            ></button>
          </div>
          <div className="modal-body">
            <form onSubmit={guardarBedel}>
              <div className="mb-3">
                <label htmlFor="nombre" className="form-label">
                  Nombre
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="nombre"
                  placeholder="Ingrese su nombre"
                  required
                  value={nombre}
                  onChange={(e) => setNombre(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="apellido" className="form-label">
                  Apellido
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="apellido"
                  placeholder="Ingrese su apellido"
                  value={apellido}
                  onChange={(e) => setApellido(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="turno" className="form-label">
                  Turno
                </label>
                <select
                  className="form-select"
                  id="turno"
                  required
                  value={turno}
                  onChange={manejarCambioTurno}
                >
                  <option value="">Seleccione su turno</option>
                  <option value="MAÑANA">Mañana</option>
                  <option value="TARDE">Tarde</option>
                  <option value="NOCHE">Noche</option>
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="nombreUsuario" className="form-label">
                  Nombre de Usuario
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="nombreUsuario"
                  placeholder="Nombre de usuario"
                  value={nombreUsuario}
                  onChange={(e) => setNombreUsuario(e.target.value)}
                  disabled={!!bedel}
                />
              </div>

              <div className="mb-3">
                <label htmlFor="password" className="form-label">
                  Contraseña
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  placeholder="Ingrese una contraseña"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="confirmarPassword" className="form-label">
                  Confirmar Contraseña
                </label>
                <input
                  type="password"
                  className="form-control"
                  id="confirmarPassword"
                  placeholder="Confirme la contraseña"
                  required
                  value={confirmarPassword}
                  onChange={(e) => setConfirmarPassword(e.target.value)}
                />
              </div>

              {password !== confirmarPassword &&
                password &&
                confirmarPassword && (
                  <div className="text-danger">
                    Las contraseñas no coinciden.
                  </div>
                )}

              <div className="d-flex justify-content-center">
                <button
                  type="button"
                  className="btn btn-light me-2"
                  onClick={manejarCancelar}
                >
                  Cancelar
                </button>

                <button type="submit" className="btn btn-warning">
                  {bedel ? "Actualizar" : "Guardar"} Bedel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ModalBedel;