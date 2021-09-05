import React, {useEffect, useState} from "react";
import axios from "axios";

import './ConversationList.css';
import useInterval from "../../UseInterval";
import Toolbar from "../Toolbar";
import CreateChat from "../CreateChat";

export default function ConversationList(props) {

    const [conversations, setConversations] = useState([]);
    const {userId} = props;

    useEffect(() => {
        getConversations()
    },[]);

    useInterval(() => {
        getConversations()
    }, 2000);

    const getConversations = () => {
        axios.get('http://localhost:8080/api/user/' + userId + '/chats').then(response => {
            let newConversations = response.data.map(result => {
                return {
                    id: result.id,
                    photo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRr5CDP3rQWwrz-BKdGJwxvZyaXYldKlnCz-qIrjDd116lf5ZbBrE0ySixHibrS0VH0lNU&usqp=CAU',
                    name: result.name,
                    lastMessage: result.lastMessage
                };
            });
            setConversations([...newConversations])
        });
    }

    return (
        <div className="conversation-list">
            <Toolbar
                // leftItem={[ ]}
                // title={}
                rightItem={<CreateChat getChatId={props.getChatId} />}
            />
            {
                conversations.map(conversation =>
                    <div className="conversation-list-item"
                         key={conversation.id} onClick={()=>props.getChatId(conversation.id)}>
                        <img className="conversation-photo" src={conversation.photo} alt="conversation" />
                        <div className="conversation-info">
                            <h1 className="conversation-title">{conversation.name}</h1>
                            <p className="conversation-snippet">{conversation.lastMessage}</p>
                        </div>
                    </div>
                )
            }
        </div>
    )
}