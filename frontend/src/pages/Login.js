import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios"; 
import "../App.css";

function Login() {
  const [usuario, setUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const navigate = useNavigate();

  const manejarEnvio = async (e) => {
    e.preventDefault();
  
    try {
      const response = await axios.post(
        "http://localhost:8080/login/iniciarSesion",
        {
          nombreUsuario: usuario,
          contrasenia: contrasena,
        }
      );
  
      alert(response.data.mensaje);
  
      if (response.data.habilitado) {
        localStorage.setItem("tipoUsuario", response.data.tipoUsuario);
        localStorage.setItem("nombreUsuario", response.data.nombreUsuario); 
  
        if (response.data.tipoUsuario === "ADMINISTRADOR") {
          navigate("/admin");
        } else if (response.data.tipoUsuario === "BEDEL") {
          navigate("/bedel");
        }
      }
    } catch (err) {
      alert("Hubo un error al intentar iniciar sesión.");
    }
  };
  

  return (
    <div
      className="container d-flex justify-content-center align-items-center"
      style={{ flex: 1, marginTop: "50px" }}
    >
      <div className="form-container encuadro inicio-sesion-cuadro col-md-6">
        <h2 className="text-center mb-4">INICIO DE SESION</h2>
        <form onSubmit={manejarEnvio}>
          <div className="mb-3">
            <label htmlFor="usuario" className="form-label">
              Nombre de Usuario
            </label>
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
            <label htmlFor="contrasena" className="form-label">
              Contraseña
            </label>
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
            <input
              className="form-check-input"
              type="checkbox"
              id="rememberMe"
            />
            <label
              className="form-check-label custom-form-check"
              htmlFor="rememberMe"
            >
              Recordarme
            </label>
          </div>
          <button type="submit" className="btn btn-dark w-100">
            Ingresar
          </button>
        </form>
      </div>
    </div>
  );
}

export default Login;
