import axios from 'axios';
import authHeader from './auth-header';
import API_LIST from '../API';

const API_URL = API_LIST + '/test/';//Ruta no existe
class UserService {
  getPublicContent() {
    return axios.get(API_URL + 'all');
  }

  getUserBoard() {
    return axios.get(API_URL + 'user', { headers: authHeader() });
  }

  getModeratorBoard() {
    return axios.get(API_URL + 'mod', { headers: authHeader() });
  }

  getAdminBoard() {
    return axios.get(API_URL + 'admin', { headers: authHeader() });
  }

  getPerfil(){
    return axios.get(API_URL + '')
  }
  
}

export default new UserService();
