import React, {useEffect, useRef, useState} from 'react';
import axios from "axios";

import './Compose.css';

export default function Compose({chatId}) {

    useEffect(() => {
        textInput.current.value = ""
        textInput.current.focus()
    },[chatId])

    const [message, setMessage] = useState('');

    const textInput = useRef(null);

    const sendMessage = (e) => {
        e.preventDefault();
        textInput.current.value = ""
        axios({
            method: 'post',
            url: 'http://localhost:8080/api/chat/' + chatId,
            data: {
                text: message
            }
        })
    }

    return (
        <div className="compose">
            <form onSubmit={sendMessage} >
                <input
                    type="text"
                    className="compose-input"
                    autoFocus="autofocus"
                    ref={textInput}
                    placeholder="Type a message..."
                    onChange={event => setMessage(event.target.value)}
                />
            </form>
        </div>
    )
}