import React, { useState, useEffect } from 'react';
import NewItem from '../NewItem';
import API_LIST from '../../API';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button, TableBody, CircularProgress } from '@mui/material';
import Moment from 'react-moment';
import authHeader from '../../service/auth-header';
import AuthService from '../../service/auth.service';
import "./TodoList.css";

function TodoList() {
    const [isLoading, setLoading] = useState(false);
    const [isInserting, setInserting] = useState(false);
    const [items, setItems] = useState([]);
    const [error, setError] = useState();
    const [isEditing, setEditing] = useState(false);

    const currentUser = AuthService.getCurrentUser();

    function deleteItem(deleteId) {

        fetch(`${API_LIST}/empleado-tarea/${currentUser.id_empleado}/${deleteId}`, {
            method: 'DELETE',
            headers: authHeader(),
            mode: 'cors'
        })
        .then(response => { 
            if (response.ok) {
                return response;
            } else {
                throw new Error('Something went wrong ...');
            }
        })
            
        fetch(`${API_LIST}/tarea/${deleteId}`, {
            method: 'DELETE',
            headers: authHeader(),
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response;
            } else {
                throw new Error('Something went wrong ...');
            }
        })
        .then(
            () => {
                const remainingItems = items.filter(item => item.id_tarea !== deleteId);
                setItems(remainingItems);
            },
            (error) => {
                setError(error);
            }
        );
    }

    function toggleDone(event, item, done) {
        event.preventDefault();
        const updatedItem = { ...item, estado: done };
        modifyItem(updatedItem, done).then(
            () => { reloadOneItem(item.id_tarea); },
            (error) => { setError(error); }
        );
    }

    function reloadOneItem(id) {
        fetch(`${API_LIST}/tarea/${id}`, {
            headers: authHeader(),
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Something went wrong ...');
            }
        })
        .then(
            (result) => {
                const updatedItems = items.map(
                    x => (x.id_tarea === id ? {
                        ...x,
                        'nombre_tarea': result.nombre_tarea,
                        'estado': result.estado
                    } : x)
                );
                setItems(updatedItems);
            },
            (error) => {
                setError(error);
            }
        );
    }

    const handleTextChange = (it, newText, cambio) => {
        const updatedItems = items.map(item => {
            if (item.id_tarea === it.id_tarea) {
                if (cambio === 'descripcion') {
                    return { ...item, descripcion_tarea: newText };
                } else if (cambio === 'nombre') {
                    return { ...item, nombre_tarea: newText };
                }
            }
            return item;
        });
        setItems(updatedItems);
    
        fetch(`/api/tarea/${it.id_tarea}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(it),
        })
        .then(response => response.json())
        .then(data => console.log('Success:', data))
        .catch((error) => console.error('Error:', error));
    };
    
    const toggleEditing = (id_tarea) => {
        const updatedItems = items.map(item => 
            item.id_tarea === id_tarea ? { ...item, isEditing: !item.isEditing } : item
        );
        setItems(updatedItems);
    };

    async function modifyItem(item, done) {
        const response = await fetch(`${API_LIST}/tarea/${item.id_tarea}`, {
            method: 'PUT',
            headers: {
                ...authHeader(),
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            body: JSON.stringify({ ...item, estado: done })
        });
        if (response.ok) {
            return response;
        } else {
            throw new Error('Something went wrong ...');
        }
    }

    useEffect(() => {
        setLoading(true);
        const ruta = (currentUser.posicion === "Desarrollador" ? `/empleado-tarea/tareas/${currentUser.id_empleado} `
            : `/tarea/equipo/${currentUser.equipo}`);
        console.log(ruta);
        fetch(`${API_LIST}${ruta}` , {
            headers: authHeader(),
            mode: 'cors'
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Something went wrong ...');
            }
        })
        .then(
            (result) => {
                setLoading(false);
                setItems(result.map(item => ({ ...item, isEditing: false })));
            },
            (error) => {
                setLoading(false);
                setError(error);
            }
        );
    }, []);

    function addItem(nombre, desc) {
        setInserting(true);
        const data = {
            nombre_tarea: nombre,
            descripcion_tarea: desc,
            estado: "Pendiente",
            id_sprint: null,
            fecha_creacion: new Date(),
            fecha_modificacion: new Date()
        };
    
        fetch(`${API_LIST}/tarea`, {
            method: 'POST',
            headers: {
                ...authHeader(),
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            body: JSON.stringify(data),
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Something went wrong ...');
            }
        })
        .then(responseData => {
            const id_tarea = responseData.id_tarea;
            const newItem = { ...data, id_tarea };
            setItems([newItem, ...items]);
    
            return fetch(`${API_LIST}/empleado-tarea/${currentUser.id_empleado}/${id_tarea}`, {
                method: 'POST',
                headers: authHeader(),
                mode: 'cors'
            });
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Something went wrong with the second request ...');
            }
            setInserting(false);
        })
        .catch(error => {
            setInserting(false);
            setError(error);
        });
    }

    return (
        <div className="todolist-home">
            <h1>MY TODO LIST</h1>
            {error ?
                <p>Error: {error.message}</p> :
                <div>
                    {isLoading && <CircularProgress />}
                    {!isLoading && 
                    <div id="maincontent">
                        <NewItem addItem={addItem} isInserting={isInserting} />
                        <table id="itemlistNotDone" className="itemlist">
                        <TableBody className='tabla'>
                            {items.map(item => (
                                item.estado !== "Hecho" && (
                                <div key={item.id_tarea}>
                                    <table className='div-table'>
                                        <div className='task'>
                                            {item.isEditing ? (
                                                <div>
                                                    <tr className="estado">{item.estado}</tr>
                                                    <tr className="title">
                                                        <input type="text" value={item.nombre_tarea} onChange={(e) => handleTextChange(item, e.target.value, "nombre")}/>
                                                    </tr>
                                                    <tr>
                                                        <textarea type="text" value={item.descripcion_tarea} onChange={(e) => handleTextChange(item, e.target.value, "descripcion")} style={{ width: '90%', height: 'auto' }}/>
                                                    </tr>
                                                    <td className="date"><Moment format="MMM Do hh:mm a">{item.fecha_creacion}</Moment></td>
                                                    {currentUser.posicion !== "Manager" && (
                                                    <>
                                                    <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item, "Hecho")} size="small">Done</Button></td>
                                                    <td><Button variant="contained" className="DoneButton" onClick={() => toggleEditing(item.id_tarea)} size="small">Save</Button></td>
                                                    </>
                                                    )}
                                                </div>
                                            ) : (
                                                <div>
                                                    <tr className="estado">{item.estado}</tr>
                                                    <tr className="title">{item.nombre_tarea}</tr>
                                                    <tr>
                                                        <p className='description'>{item.descripcion_tarea}</p>
                                                    </tr>
                                                    <td className="date"><Moment format="MMM Do hh:mm a">{item.fecha_creacion}</Moment></td>
                                                    {currentUser.posicion !== "Manager" && (
                                                    <>
                                                    <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item, "Hecho")} size="small">Done</Button></td>
                                                    <td><Button variant="contained" className="DoneButton" onClick={() => toggleEditing(item.id_tarea)} size="small">Save</Button></td>
                                                    </>
                                                    )}
                                                </div>
                                            )}
                                        </div>
                                    </table>
                                </div>
                            )))}
                        </TableBody>
                        </table>
                        {items.filter(item => item.estado !== "Hecho").length < 2 && (
                            Array.from({ length: 3 - items.filter(item => item.estado !== "Hecho").length }).map((_, index) => (
                                <div key={`empty-${index}`} className='div-table'>
                                    <br/>
                                    <br/>
                                    <br/>
                                </div>
                            ))
                        )}
                        <h2 id="donelist">Done items</h2>
                        <table id="itemlistDone" className="itemlist">
                            <TableBody>
                                {items.map(item => (
                                    item.estado === "Hecho" && (
                                    <tr key={item.id_tarea}>
                                        <td >{item.nombre_tarea}</td>
                                        <td className="date"><Moment format="MMM Do hh:mm a">{item.fecha_creacion}</Moment></td>
                                        {currentUser.posicion !== "Manager" && (
                                            <>
                                        <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item, "Pendiente")} size="small">Undo</Button></td>
                                        <td><Button startIcon={<DeleteIcon />} variant="contained" className="DeleteButton" onClick={() => deleteItem(item.id_tarea)} size="small">Delete  </Button></td>
                                            </>
                                        )}
                                    </tr>
                                )))}
                            </TableBody>
                        </table>
                    </div>}
                </div>
            }
        </div>
    );
}

export default TodoList;
