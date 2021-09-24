import React, {useEffect, useState} from 'react';
import ConversationList from "../ConversationList";
import MessageList from "../MessageList";
import axios from "axios";

import './App.css';
import "antd/dist/antd.css";

export default function App() {

    const [chatId, setChatId] = useState();
    const [userId, setUserId] = useState();

    useEffect(() => {
        getUserId();
    },[userId])

    const getUserId = () => {
        axios.get('http://localhost:8080/api/user/whoami').then(response => {
            setUserId(response.data)
        });
    }

    const getChatId = (chatId) => {
        setChatId(chatId);
    };

    const renderMessageList = () => {
        if (chatId != null) {
            return <MessageList userId={userId} chatId={chatId}/>
        } else {
            return <div>Choose a dialog</div>
        }
    };

    const renderConversationList = () => {
        if (userId != null) {
            return <ConversationList userId={userId} getChatId={getChatId}/>
        }
    };

    return (
        <div className="messenger">
            <div className="scrollable sidebar">
                {renderConversationList()}
            </div>
            <div className="scrollable sidebar">
                {renderMessageList()}
            </div>
        </div>
    );
}