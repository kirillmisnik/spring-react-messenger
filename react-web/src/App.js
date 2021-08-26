import React, {useEffect, useState} from 'react';
import axios from "axios";
import moment from "moment";
import Message from "./Message";
import useInterval from "./UseInterval"
import './App.css'

const MY_USER_ID = 1;

export default function App() {

    useEffect(() => {
        getConversations()
    },[])

    useInterval(() => {
        getConversations()
        getMessages()
    }, 100);

    // Conversations

    const [conversations, setConversations] = useState([]);
    const [chat, setChat] = useState(0);

    const getConversations = () => {
        axios.get('http://localhost:8080/api/1.0/user/1/chats').then(response => {
            let newConversations = response.data.map(result => {
                return {
                    id: result.chatId,
                    photo: 'https://component-creator.com/images/testimonials/defaultuser.png',
                    name: result.chatName,
                    text: result.lastMessage
                };
            });
            setConversations([...newConversations])
        });
    }

    // Read messages

    const [messages, setMessages] = useState([])

    const getMessages = () => {
        axios.get('http://localhost:8080/api/1.0/chat/' + chat + '/messages/all').then(response => {
            let newMessages = response.data.map(result => {
                return {
                    id: result.messageId,
                    author: result.senderId,
                    message: result.text,
                    timestamp: result.creationDate
                };
            });
            setMessages([...newMessages])
        });
    }

    const renderMessages = () => {
        let i = 0;
        let messageCount = messages.length;
        let tempMessages = [];

        while (i < messageCount) {
            let previous = messages[i - 1];
            let current = messages[i];
            let next = messages[i + 1];
            let isMine = current.author === MY_USER_ID;
            let currentMoment = moment(current.timestamp);
            let prevBySameAuthor = false;
            let nextBySameAuthor = false;
            let startsSequence = true;
            let endsSequence = true;
            let showTimestamp = true;

            if (previous) {
                let previousMoment = moment(previous.timestamp);
                let previousDuration = moment.duration(currentMoment.diff(previousMoment));
                prevBySameAuthor = previous.author === current.author;

                if (prevBySameAuthor && previousDuration.as('hours') < 1) {
                    startsSequence = false;
                }

                if (previousDuration.as('hours') < 1) {
                    showTimestamp = false;
                }
            }

            if (next) {
                let nextMoment = moment(next.timestamp);
                let nextDuration = moment.duration(nextMoment.diff(currentMoment));
                nextBySameAuthor = next.author === current.author;

                if (nextBySameAuthor && nextDuration.as('hours') < 1) {
                    endsSequence = false;
                }
            }

            tempMessages.push(
                <Message
                    key={i}

                    chatType={chatInfo.get("chatType")}
                    senderName={current.author}

                    isMine={isMine}
                    startsSequence={startsSequence}
                    endsSequence={endsSequence}
                    showTimestamp={showTimestamp}
                    data={current}
                />
            );
            i += 1;
        }

        return tempMessages;
    }

    // Send messages

    const [message, setMessage] = useState('')

    const textInput = React.useRef();

    const sendMessage = (e) => {
        e.preventDefault();
        textInput.current.value = ""
        scrollToBottom()
        axios({
            method: 'post',
            url: 'http://localhost:8080/api/1.0/chat/' + chat,
            data: {
                text: message
            }
        })
    }

    // Chat info

    const [chatInfo, setChatInfo] = useState(new Map())

    const getChatInfo = (id) => {
        axios.get('http://localhost:8080/api/1.0/chat/' + id).then(response => {
            let newChatInfo = new Map()
            newChatInfo.set("chatName", response.data.chatName)
            newChatInfo.set("chatType", response.data.chatType)
            setChatInfo(newChatInfo)
        });
    }

    const currentChat = (id) => {
        setChat(id)
        getChatInfo(id)
        scrollToBottom()
    }

    let messagesEnd;

    const scrollToBottom = () => {
        messagesEnd.scrollIntoView({ behavior: "smooth" });
    }

    return (
        <div className="messenger">

            <div className="scrollable sidebar">
                <div className="conversation-list">
                    <div className="toolbar">
                        <div className="left-items">
                            <i className='toolbar-button ion-ios-cog' />
                        </div>
                        <h1 className="toolbar-title">
                            <div className="conversation-search">
                                <input
                                    type="search"
                                    className="conversation-search-input"
                                    placeholder="Search Messages"
                                />
                            </div>
                        </h1>
                        <div className="right-items">
                            <i className='toolbar-button ion-ios-add-circle-outline' />
                        </div>
                    </div>
                    <div>
                        {
                            conversations.map(conversation =>
                                <div className="conversation-list-item" onClick={()=>currentChat(conversation.id) }>
                                    <img className="conversation-photo" src={ conversation.photo } alt="conversation" />
                                    <div className="conversation-info">
                                        <h1 className="conversation-title">{ conversation.name }</h1>
                                        <p className="conversation-snippet">{ conversation.text }</p>
                                    </div>
                                </div>
                            )
                        }
                    </div>
                </div>
            </div>

            <div className="scrollable content">
                <div className="message-list">
                    <div className="toolbar">
                        <div className="left-items"/>
                        <h1 className="toolbar-title">
                            { chatInfo.get("chatName") }
                        </h1>
                        <div className="right-items">
                            <i className='toolbar-button ion-ios-information-circle-outline' />
                        </div>
                    </div>
                    <div className="message-list-container">{ renderMessages() }</div>
                    <div style={{ float:"left", clear: "both" }}
                         ref={(el) => { messagesEnd = el; }}>
                    </div>

                    <div className="compose">
                        <form onSubmit={ sendMessage } className="compose">
                            <input
                                type="text"
                                className="compose-input"
                                ref={textInput}
                                placeholder="Type a message"
                                onChange={ event => setMessage(event.target.value) }
                            />
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}