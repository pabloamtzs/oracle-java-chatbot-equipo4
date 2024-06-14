import React, { Component} from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Home.css";
import AuthService from "./service/auth.service";
// import AuthVerify from "./common/auth-verify";
import EventBus from "./common/EventBus";
import TodoList from "./components/TodoList/TodoList";
import Perfil from "./components/profile/Perfil";
class Home extends Component {

      
      render() {
        //const { currentUser, showModeratorBoard, showAdminBoard } = this.state;
    
        return (
          <div>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/boxicons/2.1.1/css/boxicons.min.css"></link>
            <div className="body-home">
            <nav>
              <div className="sidebar">
                  <div className="logo-details">
                      <img src="/icons/Chatbot Logo.png" alt="Logo Chatbot" />
                      <span className="logo_name">Chatbot E4</span>
                  </div>
                  <ul className="nav-links">
                      <li>
                          <Link to="/home/todolist">
                              <i className='bx bxs-dashboard'></i>
                              <span className="link_name">Dashboard</span>
                          </Link>
                      </li>
                      <li>
                          <a href="https://web.telegram.org/a/#7199106217" target="_blank" rel="noopener noreferrer">
                              <i className='bx bxs-message-dots'></i>
                              <span className="link_name">Chatbot</span>
                          </a>
                      </li>
                  </ul>
                  <Link to="/home/profile">
                    <div className="profile-details">
                            <i className='bx bxs-user-circle' ></i>
                    </div>
                  </Link>
                  <a href="/login">
                    <div className="logout-details">
                            <i className='bx bx-log-out'></i>
                    </div>
                  </a>
              </div>
            </nav>
                
              <div className="background">
              </div>            
                <Routes>
                  <Route path='/todolist'  element={<TodoList/>} />
                  <Route path='/profile'  element={<Perfil/>} />
                  <Route path='/'  element={<Perfil/>} />
                </Routes>
            </div>
              {/* <AuthVerify logOut={this.logOut}/> */}
          </div>
        );
      }
}
export default Home;