import React, { useState, useEffect } from 'react';

const ModalBedel = ({ cerrar, bedel, actualizarBedeles }) => {
  const [nombre, setNombre] = useState('');
  const [apellido, setApellido] = useState(''); // Definir el estado para el apellido
  const [nombreUsuario, setNombreUsuario] = useState(''); // Definir el estado para el nombre de usuario
  const [turno, setTurno] = useState('');
  const [password, setPassword] = useState('');
  const [confirmarPassword, setConfirmarPassword] = useState('');

  // Si estamos editando un bedel, cargar los valores en el formulario
  useEffect(() => {
    if (bedel) {
      setNombre(bedel.nombre);
      setApellido(bedel.apellido || ''); // Asegurarse de manejar el caso donde no haya apellido
      setNombreUsuario(bedel.nombreUsuario || ''); // Asegurarse de manejar el caso donde no haya nombreUsuario
      setTurno(bedel.turno || ''); // Establecer el turno si está disponible
    }
  }, [bedel]);

  // Funcion para guardar el bedel (nuevo o actualizado)
  const guardarBedel = async (e) => {
    e.preventDefault();

    // Validar que las contraseñas coincidan
    if (password !== confirmarPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    const nuevoBedel = {
      ...(bedel?.id && { idUsuario: bedel.id }), // Solo agregar idUsuario si bedel tiene id
      nombre,
      apellido,
      nombreUsuario,
      contrasenia: password,
      repetirContrasenia: confirmarPassword,
      tipoUsuario: "BEDEL", // Siempre será "BEDEL"
      tipoTurno: turno,  // El turno (MANANA, TARDE, NOCHE)
    };

    try {
      // Si el bedel tiene id, es una actualizacion (PUT), si no, es un nuevo registro (POST)
      const respuesta = bedel?.id
        ? await fetch(`http://localhost:8080/usuarios/bedel/${bedel.id}`, {
            method: 'PUT',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(nuevoBedel),
          })
        : await fetch('http://localhost:8080/usuarios/crear', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(nuevoBedel),
          });

      if (respuesta.ok) {
        alert(bedel ? 'Bedel actualizado correctamente' : 'Bedel guardado correctamente');
        actualizarBedeles();  // Refrescar la lista de bedeles
        cerrar();              // Cerrar el modal
      } else {
        alert('Error al guardar el bedel');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('Hubo un problema al guardar el bedel');
    }
  };

  // Esta funcion asegura que el turno siempre este en mayusculas
  const manejarCambioTurno = (e) => {
    const turnoMayuscula = e.target.value.toUpperCase();
    setTurno(turnoMayuscula);
  };

  return (
    <div className="modal show d-block" tabIndex="-1">
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">{bedel ? 'Editar Bedel' : 'Nuevo Bedel'}</h5>
            <button type="button" className="btn-close" onClick={cerrar} aria-label="Close"></button>
          </div>
          <div className="modal-body">
            <form onSubmit={guardarBedel}>
              <div className="mb-3">
                <label htmlFor="nombre" className="form-label">Nombre</label>
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
                <label htmlFor="apellido" className="form-label">Apellido</label>
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
                <label htmlFor="nombreUsuario" className="form-label">Nombre de Usuario</label>
                <input
                  type="text"
                  className="form-control"
                  id="nombreUsuario"
                  placeholder="Ingrese su nombre de usuario"
                  required
                  value={nombreUsuario}
                  onChange={(e) => setNombreUsuario(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="turno" className="form-label">Turno</label>
                <select
                  className="form-select"
                  id="turno"
                  required
                  value={turno}
                  onChange={manejarCambioTurno} // Utilizar la funcion manejarCambioTurno
                >
                  <option value="">Seleccione su turno</option>
                  <option value="MANANA">Manana</option>
                  <option value="TARDE">Tarde</option>
                  <option value="NOCHE">Noche</option>
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">Contraseña</label>
                <input
                  type="password"
                  className="form-control"
                  id="password"
                  placeholder="Ingrese su contraseña"
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
              <div className="mb-3">
                <label htmlFor="confirmarPassword" className="form-label">Confirmar Contraseña</label>
                <input
                  type="password"
                  className="form-control"
                  id="confirmarPassword"
                  placeholder="Confirme su contraseña"
                  required
                  value={confirmarPassword}
                  onChange={(e) => setConfirmarPassword(e.target.value)}
                />
              </div>
              <div className="d-flex justify-content-center">
                <button type="button" className="btn btn-light me-2" onClick={cerrar}>Cancelar</button>
                <button type="submit" className="btn btn-success">{bedel ? 'Actualizar' : 'Guardar'} Bedel</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ModalBedel;

