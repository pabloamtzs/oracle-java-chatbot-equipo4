import React, { Component } from 'react'
import { Routes, Route } from 'react-router-dom'
import Login from './components/login/login.component'
import "bootstrap/dist/css/bootstrap.min.css";
import TodoList from './components/TodoList/TodoList';
import Home from './Home';

export default class App extends Component {
  render() {
    return (
      <div>
        <div className="container mt-3">
          <Routes>
            <Route path="/home/*" element={<Home />} />
            <Route path="/login" element={<Login/>} />
            <Route path="/" element={<Login />} />
            <Route path='todolist' element={<TodoList />} />
          </Routes>
        </div>
        
      </div>
    )
  }
}
