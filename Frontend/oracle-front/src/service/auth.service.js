import API_LIST from '../API';

const headers = {
  'Content-Type': 'application/json',
  'Access-Control-Allow-Origin': '*',
  'Access-Control-Allow-Headers': 'X-Requested-With'
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

async createUser(email){
    const response = await fetch(API_LIST + '/empleado/email/' + email, {
      method: 'GET', headers: headers, mode: 'cors'
    });
    if (!response.ok) {
      throw new Error('user failed');
    }
    const data = await response.json();
    console.log(data);
    return data;
};

  logout() {
    localStorage.removeItem("usuario");
    localStorage.removeItem("authToken");
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('usuario'));;
  }

  getAuthToken() {
    return localStorage.getItem('authToken');
  }
}

export default new AuthService();
