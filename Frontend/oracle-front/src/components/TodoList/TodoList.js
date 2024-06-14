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

    const currentUser = AuthService.getCurrentUser();

    function deleteItem(deleteId) {
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
        fetch(`${API_LIST}/tarea`, {
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
                setItems(result);
            },
            (error) => {
                setLoading(false);
                setError(error);
            }
        );
    }, []);

    function addItem(nombre, desc) {
        setInserting(true);
        const data = { "nombre_tarea": nombre, "descripcion_tarea": desc,"estado": "Pendiente", "id_sprint": null, "fecha_creacion": new Date(), "fecha_modificacion": new Date() };
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
                return response.headers.get('location');
            } else {
                throw new Error('Something went wrong ...');
            }
        })
        .then(
            (location) => {
                const newItem = { ...data, id_tarea: location.split('/').pop() };
                setItems([newItem, ...items]);
                setInserting(false);
            },
            (error) => {
                setInserting(false);
                setError(error);
            }
        );
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
                                                <tr className="estado">{item.estado}</tr>
                                                <tr className="title">{item.nombre_tarea}</tr>
                                                <tr>
                                                    <p className='description'>{item.descripcion_tarea}</p>
                                                </tr>
                                                <td className="date"><Moment format="MMM Do hh:mm a">{item.fecha_creacion}</Moment></td>
                                                <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item, "Hecho")} size="small">Done</Button></td>
                                                <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item, "Hecho")} size="small">Edit</Button></td>
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

                                        <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item, "Pendiente")} size="small">Undo</Button></td>
                                        <td><Button startIcon={<DeleteIcon />} variant="contained" className="DeleteButton" onClick={() => deleteItem(item.id_tarea)} size="small">Delete  </Button></td>
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
