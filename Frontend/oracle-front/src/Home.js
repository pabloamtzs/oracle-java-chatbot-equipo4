import React, { Component} from "react";
import { Routes, Route, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./Home.css";
import AuthService from "./service/auth.service";
// import AuthVerify from "./common/auth-verify";
import EventBus from "./common/EventBus";
import TodoList from "./components/TodoList/TodoList";
class Home extends Component {
    constructor(props) {
        super(props);
        this.logOut = this.logOut.bind(this);
    
        this.state = {
          showModeratorBoard: false,
          showAdminBoard: false,
          currentUser: undefined,
        };

        //this.postData()
        //console.log(this.createUser("a00835387@tec.mx"))
      }
      
    componentDidMount() {
        const user = AuthService.getCurrentUser();
    
        if (user) {
          this.setState({
            currentUser: user,
            showModeratorBoard: user.roles.includes("ROLE_MODERATOR"),
            showAdminBoard: user.roles.includes("ROLE_ADMIN"),
          });
        }
        
        EventBus.on("logout", () => {
          this.logOut();
        });
      }

      
        componentWillUnmount() {
            EventBus.remove("logout");
        }

        logOut() {
            AuthService.logout();
            this.setState({
            showModeratorBoard: false,
            showAdminBoard: false,
            currentUser: undefined,
            });
        }
        
      
      render() {
        const { currentUser, showModeratorBoard, showAdminBoard } = this.state;
    
        return (
          <div>
            <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/boxicons/2.1.1/css/boxicons.min.css"></link>
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
                          <a href="https://web.telegram.org/a/#7199106217">
                              <i className='bx bxs-message-dots'></i>
                              <span className="link_name">Chatbot</span>
                          </a>
                      </li>
                  </ul>
                  <div className="profile-details">
                          <i className='bx bxs-user-circle' ></i>
                  </div>
                  <a href="/login">
                    <div className="logout-details">
                            <i className='bx bx-log-out'></i>
                    </div>
                  </a>
              </div>
          </nav>
              
          <div className="menu">
              <Routes>
                <Route path='/todolist'  element={<TodoList/>} />
              </Routes>
              </div>            
            {/* <AuthVerify logOut={this.logOut}/> */}
          </div>
        );
      }
}
export default Home;