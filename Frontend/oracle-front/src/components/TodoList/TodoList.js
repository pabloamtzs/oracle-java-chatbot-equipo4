          /*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * This is the application main React component. We're using "function"
 * components in this application. No "class" components should be used for
 * consistency.
 * @author  jean.de.lavarene@oracle.com
 */
import React, { useState, useEffect } from 'react';
import NewItem from '../NewItem';
import API_LIST from '../../API';
import DeleteIcon from '@mui/icons-material/Delete';
import { Button, TableBody, CircularProgress } from '@mui/material';
import Moment from 'react-moment';
import authHeader from '../../service/auth-header';
import "./TodoList.css";
/* In this application we're using Function Components with the State Hooks
 * to manage the states. See the doc: https://reactjs.org/docs/hooks-state.html
 * This Home component represents the entire app. It renders a NewItem component
 * and two tables: one that lists the todo items that are to be done and another
 * one with the items that are already done.
 */
function TodoList() {
    // isLoading is true while waiting for the backend to return the list
    // of items. We use this state to display a spinning circle:
    const [isLoading, setLoading] = useState(false);
    // Similar to isLoading, isInserting is true while waiting for the backend
    // to insert a new item:
    const [isInserting, setInserting] = useState(false);
    // The list of todo items is stored in this state. It includes the "done"
    // "not-done" items:
    const [items, setItems] = useState([]);
    // In case of an error during the API call:
    const [error, setError] = useState();

    function deleteItem(deleteId) {
      // console.log("deleteItem("+deleteId+")")
      fetch(API_LIST+ "/tareas"+deleteId, {
        method: 'DELETE',
        headers: authHeader(),
        mode: 'cors'
      })
      .then(response => {
        // console.log("response=");
        // console.log(response);
        if (response.ok) {
          // console.log("deleteItem FETCH call is ok");
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      })
      .then(
        (result) => {
          const remainingItems = items.filter(item => item.id !== deleteId);
          setItems(remainingItems);
          window.location.reload();
        },
        (error) => {
          setError(error);
        }
      );
    }
    function toggleDone(event, id, nombre, done) {
      event.preventDefault();
      modifyItem(id, nombre, done).then(
        (result) => { reloadOneItem(id); },
        (error) => { setError(error); }
      );
    }
    function reloadOneItem(id){
      fetch(API_LIST+"/tarea/"+id,{
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
            const items2 = items.map(
              x => (x.id === id ? {
                 ...x,
                 'nombre_tarea':result.nombre_tarea,
                 'estado': result.estado
                } : x));
            setItems(items2);
            window.location.reload();
          },
          (error) => {
            setError(error);
          });
    }
    async function modifyItem(id, nombre, done) {
      // console.log("deleteItem("+deleteId+")")
      console.log("Id a trabajar: " + id);
      var data = {"nombre_tarea": nombre, "estado": done, "id_sprint": null};
      const response = await fetch(API_LIST + "/tarea/" + id, {
        method: 'PUT',
        headers: authHeader(),
        mode: 'cors',
        body: JSON.stringify(data)
      });
      // console.log("response=");
      // console.log(response);
      if (response.ok) {
        // console.log("deleteItem FETCH call is ok");
        return response;
      } else {
        throw new Error('Something went wrong ...');
      }
    }
    /*
    To simulate slow network, call sleep before making API calls.
    const sleep = (milliseconds) => {
      return new Promise(resolve => setTimeout(resolve, milliseconds))
    }
    */
    useEffect(() => {
      setLoading(true);
      // sleep(5000).then(() => {
      fetch(API_LIST + "/tarea", {
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
          });

      //})
    },
    // https://en.reactjs.org/docs/faq-ajax.html
    [] // empty deps array [] means
       // this useEffect will run once
       // similar to componentDidMount()
    );
    function addItem(text){
      console.log("addItem("+text+")");
      setInserting(true);
      var data = {"nombre_tarea": text, "estado": "Pendiente", "id_sprint": null, "fecha_creacion": new Date()};
      console.log(data);
      fetch(API_LIST + "/tarea", {
        method: 'POST',
        // We convert the React state to JSON and send it as the POST body
        headers: authHeader(),
        mode: 'cors',
        body: JSON.stringify(data),
      }).then((response) => {
        // This API doens't return a JSON document
        console.log(response);
        console.log();
        console.log(response.headers.location);
        // return response.json();
        if (response.ok) {
          return response;
        } else {
          throw new Error('Something went wrong ...');
        }
      }).then(
        (result) => {
          var id = result.headers.get('location');
          var newItem = {};
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
        
        { error ?
          <p>Error: {error.message}</p>:
          <div>
            { isLoading &&
              <CircularProgress />
            }
            { !isLoading &&
            <div id="maincontent">
            <NewItem addItem={addItem} isInserting={isInserting}/>
            <table id="itemlistNotDone" className="itemlist">
              <TableBody className='tabla'>
              {items.map(item => (
                !item.done && (
                <div>
                  <table key={item.id_tarea}>
                    <div className='task'>
                    <tr className="estado">{item.estado}</tr>
                    <tr className="title">{item.nombre_tarea}</tr>
                    { /*<td>{JSON.stringify(item, null, 2) }</td>*/ }
                    <tr>
                      <p className='description'>{item.descripcion_tarea}</p>
                    </tr>
                    <td className="date"><Moment format = "MMM Do hh:mm a">{item.fecha_creacion}</Moment></td>
                    <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item.id_tarea, item.nombre_tarea, "Hecho", item.id_sprint)} size="small">
                          Done
                        </Button></td>
                    <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item.id_tarea, item.nombre_tarea, "Hecho", item.id_sprint)} size="small">
                      Edit
                    </Button></td>
                    </div>
                  </table>
                </div>
              )))}
              </TableBody>
            </table>
            <h2 id="donelist">
              Done items
            </h2>
            <table id="itemlistDone" className="itemlist">
              <TableBody>
              {items.map(item => (
                item.done && (
                <tr key={item.id_tarea}>
                  <td className="description">{item.nombre_tarea}</td>
                  <td className="date"><Moment format="MMM Do hh:mm a">{item.createdAt}</Moment></td>Hecho
                  <td><Button variant="contained" className="DoneButton" onClick={(event) => toggleDone(event, item.id_tarea, item.nombre_tarea, "Pendiente")} size="small">
                        Undo
                      </Button></td>
                  <td><Button startIcon={<DeleteIcon />} variant="contained" className="DeleteButton" onClick={() => deleteItem(item.id_tarea)} size="small">
                        Delete
                      </Button></td>
                </tr>
              )))}
              </TableBody>
            </table>
            </div>
            }
          </div>
           
        }
        
        

      </div>
    );
}
export default TodoList;