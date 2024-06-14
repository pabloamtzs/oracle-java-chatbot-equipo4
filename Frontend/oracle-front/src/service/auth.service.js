import API_LIST from '../API';
import authHeader from './auth-header';

const headers = {
  'Content-Type': 'application/json',
  // 'Access-Control-Allow-Origin': '*',
  // 'Access-Control-Allow-Headers': 'X-Requested-With'
};

class AuthService {

  
  login(email, contrasena) {
    return fetch(API_LIST+ '/auth/login', {
      method: 'POST',
      headers: headers,
      body: JSON.stringify({email, contrasena}),
      mode: 'cors'})
      .then(response => {
        if (!response.ok) {
            throw new Error('Login failed');
        }
        return response.json();
    })
    .then(data => {
        console.log(data.token)
        localStorage.setItem('authToken', JSON.stringify(data.token));
        this.createUser(email).then(nombre => {
          localStorage.setItem('perfil', JSON.stringify(nombre));     
        })
        
        
        //Cookies.set('authToken', data.token);
    })
  }

  async createUser(email) {
    try {
        // Realiza la primera petición para obtener los datos del empleado
        let response = await fetch(API_LIST + '/empleado/email/' + email, {
            method: 'GET',
            headers: headers,
            mode: 'cors'
        });

        if (!response.ok) {
            throw new Error('No se encontró respuesta en la primera petición');
        }

        let data = await response.json();

        console.log("Empleado obtenido:", data);

        // Realiza la segunda petición usando el id_empleado obtenido de la primera petición
        response = await fetch(API_LIST + '/miembro-equipo/equipo/' + data.id_empleado, {
            method: 'GET',
            headers: authHeader(),
            mode: 'cors'
        });

        if (!response.ok) {
            throw new Error('No se encontró respuesta en la segunda petición');
        }

        const equipoData = await response.json();
        console.log("Equipo obtenido:", equipoData[0].id);
        // Combina los datos de la primera y segunda petición
        data = { ...data, equipo: equipoData[0].id.id_equipo};
        console.log("Datos completos:", data);

        return data;
    } catch (error) {
        console.error('Error en createUser:', error);
        throw error;
    }
}


  logout() {
    localStorage.removeItem("perfil");
    localStorage.removeItem("authToken");
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('perfil'));;
  }

  getAuthToken() {
    return localStorage.getItem('authToken');
  }
}

export default new AuthService();
