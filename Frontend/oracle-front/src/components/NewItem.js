/*
## MyToDoReact version 1.0.
##
## Copyright (c) 2022 Oracle, Inc.
## Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl/
*/
/*
 * Component that supports creating a new todo item.
 * @author  jean.de.lavarene@oracle.com
 */

import React, { useState } from "react";
import Button from '@mui/material/Button';


function NewItem(props) {
  const [nombre, setNombre] = useState('');
  const [desc, setDesc] = useState('');
  function handleSubmit(e) {
    // console.log("NewItem.handleSubmit("+e+")");
    if (!nombre.trim()) {
      return;
    }
    // addItem makes the REST API call:
    props.addItem(nombre, desc);
    setNombre("title");
    setDesc("description");
    window.location.reload();
    e.preventDefault();
  }
  function handleChangeTitle(e) {
    setNombre(e.target.value);
  }
  function handleChangeDesc(e) {
    setDesc(e.target.value);
  }
  return (
    <div id="newinputform">
    <form>
      <input
        id="newTitle"
        placeholder="Title"
        type="text"
        autoComplete="off"
        value={nombre}
        onChange={handleChangeTitle}
        // No need to click on the "ADD" button to add a todo item. You
        // can simply press "Enter":
        onKeyDown={event => {
          if (event.key === 'Enter') {
            handleSubmit(event);
          }
        }}
      />
      <input
        id="newDescription" 
        placeholder="Description"
        type="text"
        autoComplete="off"
        value={desc}
        onChange={handleChangeDesc}
        // No need to click on the "ADD" button to add a todo item. You
        // can simply press "Enter":
        onKeyDown={a => {
          if (a.key === 'Enter') {
            handleSubmit(a);
          }
        }}
      />
      <span>&nbsp;&nbsp;</span>
      <Button
        className="AddButton"
        variant="contained"
        disabled={props.isInserting}
        onClick={!props.isInserting ? handleSubmit : null}
        size="small"
      >
        {props.isInserting ? 'Adding…' : 'Add'}
      </Button>
    </form>
    </div>
  );
}

export default NewItem;