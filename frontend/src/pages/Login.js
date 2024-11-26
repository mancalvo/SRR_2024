import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../App.css';

function Login() {
  const [usuario, setUsuario] = useState('');
  const [contrasena, setContrasena] = useState('');
  const navigate = useNavigate();

  const manejarEnvio = (e) => {
    e.preventDefault();

    // Lógica de validación de usuarios y contraseñas
    if (usuario === 'admin' && contrasena === 'admin') {
      localStorage.setItem('tipoUsuario', 'ADMINISTRADOR');
      navigate('/admin'); // Redirige a la página de Administrador
    } else if (usuario === 'dardo' && contrasena === 'dardo') {
      localStorage.setItem('tipoUsuario', 'BEDEL');
      navigate('/bedel'); // Redirige a la página de Bedel
    } else {
      alert('Credenciales incorrectas.');
    }

    // La siguiente parte está comentada para no realizar la conexión con la base de datos
    /*
    try {
      const respuesta = await axios.post('/inicioSesion', { usuario, contrasena });
      const { estado, tipo_Usuario } = respuesta.data;

      if (estado) {
        localStorage.setItem('tipoUsuario', tipo_Usuario); // Guarda el tipo de usuario
        if (tipo_Usuario === 'ADMINISTRADOR') {
          navigate('/admin'); // Redirige a la página de Administrador
        } else if (tipo_Usuario === 'BEDEL') {
          navigate('/bedel'); // Redirige a la página de Bedel
        }
      } else {
        alert('Credenciales incorrectas.');
      }
    } catch (error) {
      console.error('Error en el inicio de sesión:', error);
      alert('Ocurrió un error, por favor intenta de nuevo.');
    }
    */
  };

  return (
    <div className="container d-flex justify-content-center align-items-center" style={{ flex: 1, marginTop: '50px' }}>
      <div className="form-container encuadro col-md-6">
        <h2 className="text-center mb-4">INICIO DE SESION</h2>
        <form onSubmit={manejarEnvio}>
          <div className="mb-3">
            <label htmlFor="usuario" className="form-label">Nombre de Usuario</label>
            <input
              type="text"
              id="usuario"
              className="form-control"
              value={usuario}
              onChange={(e) => setUsuario(e.target.value)}
              placeholder="Ingrese su nombre de usuario"
            />
          </div>
          <div className="mb-3">
            <label htmlFor="contrasena" className="form-label">Contraseña</label>
            <input
              type="password"
              id="contrasena"
              className="form-control"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              placeholder="Ingrese su contraseña"
            />
          </div>
          <div className="form-check mb-3 custom-form-check">
            <input className="form-check-input" type="checkbox" id="rememberMe" />
            <label className="form-check-label custom-form-check" htmlFor="rememberMe">Recordarme</label>
          </div>
          <button type="submit" className="btn btn-dark w-100">Ingresar</button>
        </form>
      </div>
    </div>
  );
}

export default Login;
