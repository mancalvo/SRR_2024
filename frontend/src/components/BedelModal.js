import React, { useState, useEffect } from 'react';

const BedelModal = ({ handleClose, handleSave, bedel }) => {
  const [nombre, setNombre] = useState('');
  const [turno, setTurno] = useState('');
  const [password, setPassword] = useState('');
  const [confirmarPassword, setConfirmarPassword] = useState('');

  // Si estamos editando un bedel, cargar los valores en el formulario
  useEffect(() => {
    if (bedel) {
      setNombre(bedel.nombre);
      setTurno(bedel.turno);
    }
  }, [bedel]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (password !== confirmarPassword) {
      alert('Las contraseñas no coinciden');
      return;
    }

    const newBedel = { id: bedel?.id, nombre, turno, password };
    handleSave(newBedel);
  };

  // Esta función asegura que el turno siempre esté en mayúsculas
  const handleTurnoChange = (e) => {
    const upperCaseTurno = e.target.value.toUpperCase();
    setTurno(upperCaseTurno);
  };

  return (
    <div className="modal show d-block" tabIndex="-1">
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header justify-content-center">
            <h5 className="modal-title">{bedel ? 'Editar Bedel' : 'Nuevo Bedel'}</h5>
            <button type="button" className="btn-close" onClick={handleClose} aria-label="Close"></button>
          </div>
          <div className="modal-body">
            <form onSubmit={handleSubmit}>
              <div className="mb-3">
                <label htmlFor="nombre" className="form-label">Nombre y Apellido</label>
                <input type="text" className="form-control" id="nombre" placeholder="Ingrese su nombre" required value={nombre} onChange={(e) => setNombre(e.target.value)} />
              </div>
              <div className="mb-3">
                <label htmlFor="turno" className="form-label">Turno</label>
                <select
                  className="form-select"
                  id="turno"
                  required
                  value={turno}
                  onChange={handleTurnoChange} // Utilizar la función handleTurnoChange
                >
                  <option value="">Seleccione su turno</option>
                  <option value="MAÑANA">Mañana</option>
                  <option value="TARDE">Tarde</option>
                  <option value="NOCHE">Noche</option>
                </select>
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">Contraseña</label>
                <input type="password" className="form-control" id="password" placeholder="Ingrese su contraseña" required value={password} onChange={(e) => setPassword(e.target.value)} />
              </div>
              <div className="mb-3">
                <label htmlFor="confirmarPassword" className="form-label">Confirmar Contraseña</label>
                <input type="password" className="form-control" id="confirmarPassword" placeholder="Confirme su contraseña" required value={confirmarPassword} onChange={(e) => setConfirmarPassword(e.target.value)} />
              </div>
              <div className="d-flex justify-content-center">
                <button type="button" className="btn btn-light me-2" onClick={handleClose}>Cancelar</button>
                <button type="submit" className="btn btn-success">{bedel ? 'Actualizar' : 'Guardar'} Bedel</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default BedelModal;
