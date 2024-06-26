import React, { Component, useState, useEffect } from 'react'
import AuthService from '../../service/auth.service'
import './Perfil.css'
import { Link } from 'react-router-dom';
function Perfil () {    
    
    const [currentUser, setCurrentUser] = useState(null);

    useEffect(() => {
        const user = AuthService.getCurrentUser();
        
        setCurrentUser(user);
      }, []);
    
    return (
      <div>
        {(currentUser) ?
        <div className="page-content page-container" id="page-content">
            <div className="padding">
                <div className="row container d-flex justify-content-center">
                        <div className="col-xl-10 col-md-12">
                            <div className="card user-card-full">
                                <div className="row m-l-0 m-r-0">
                                    <div className="col-sm-4 bg-c-lite-green user-profile">
                                        <div className="card-block text-center text-white">
                                            <div className="m-b-25">
                                            <i className='bx bxs-user-circle'  style={{ fontSize: '3em' }} ></i>
                                            </div>
                                            <h3 className="f-w-600">{currentUser.nombre}</h3>
                                            <h6>{currentUser.apellido}</h6>
                                            <i className=" mdi mdi-square-edit-outline feather icon-edit m-t-10 f-16"></i>
                                        </div>
                                    </div>
                                    <div className="col-sm-8">
                                        <div className="card-block">
                                            <h6 className="m-b-20 p-b-5 b-b-default f-w-600">Information</h6>
                                            <div className="row">
                                                <div className="col-sm-6">
                                                    <p className="m-b-10 f-w-600">Email</p>
                                                    <h6 className="text-muted f-w-400">{currentUser.email}</h6>
                                                </div>
                                                <div className="col-sm-6">
                                                    <p className="m-b-10 f-w-600">ID</p>
                                                    <h6 className="text-muted f-w-400">{currentUser.id_empleado}</h6>
                                                </div>
                                            </div>
                                            <h6 className="m-b-20 m-t-40 p-b-5 b-b-default f-w-600">Projecto</h6>
                                            <div className="row">
                                                <div className="col-sm-6">
                                                    <p className="m-b-10 f-w-600">Equipo</p>
                                                    <h6 className="text-muted f-w-400">{currentUser.equipo}</h6>
                                                </div>
                                                <div className="col-sm-6">
                                                    <p className="m-b-10 f-w-600">Posicion</p>
                                                    <h6 className="text-muted f-w-400">{currentUser.posicion}</h6>
                                                </div>
                                            </div>
                                            <ul className="social-link list-unstyled m-t-40 m-b-10">
                                                <li><a href="#!" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="facebook" data-abc="true"><i className="mdi mdi-facebook feather icon-facebook facebook" aria-hidden="true"></i></a></li>
                                                <li><a href="#!" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="twitter" data-abc="true"><i className="mdi mdi-twitter feather icon-twitter twitter" aria-hidden="true"></i></a></li>
                                                <li><a href="#!" data-toggle="tooltip" data-placement="bottom" title="" data-original-title="instagram" data-abc="true"><i className="mdi mdi-instagram feather icon-instagram instagram" aria-hidden="true"></i></a></li>
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            : ( <div>Error al cargar la informacion del usuario</div>)}
      </div>
    )
  }
export default Perfil;
