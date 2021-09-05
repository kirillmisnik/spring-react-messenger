import React, {useEffect, useState} from 'react';
import {Modal, Button, Select, Input} from 'antd';
import 'antd/dist/antd.css';
import axios from "axios";
import moment from "moment";
import Message from "./Message";
import useInterval from "./UseInterval"
import './App.css'
import {InfoCircleOutlined, SettingOutlined, UsergroupAddOutlined} from "@ant-design/icons";

export default function App() {

    useEffect(() => {
        whoAmI()
        getConversations()
    },[]);

    useInterval(() => {
        getConversations()
        if (chat != null) {
            getMessages()
        }
    }, 2000);

    // Who am I

    const [userId, setUserId] = useState();

    const whoAmI = () => {
        axios.get('http://localhost:8080/api/user/whoami').then(response => {
            setUserId(response.data)
        });
    }

    // Conversations

    const [conversations, setConversations] = useState([]);
    const [chat, setChat] = useState();

    const getConversations = () => {
        axios.get('http://localhost:8080/api/user/' + userId + '/chats').then(response => {
            let newConversations = response.data.map(result => {
                return {
                    id: result.id,
                    photo: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRr5CDP3rQWwrz-BKdGJwxvZyaXYldKlnCz-qIrjDd116lf5ZbBrE0ySixHibrS0VH0lNU&usqp=CAU',
                    name: result.name,
                    text: result.lastMessage
                };
            });
            setConversations([...newConversations])
        });
    }

    // Read messages

    const [messages, setMessages] = useState([])

    const getMessages = () => {
        axios.get('http://localhost:8080/api/chat/' + chat + '/messages/all').then(response => {
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
            let isMine = current.author === userId;
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
            url: 'http://localhost:8080/api/chat/' + chat,
            data: {
                text: message
            }
        })
        getMessage()
    }

    const getMessage = () => {
        getMessages()
    }

    // Chat info

    const [chatInfo, setChatInfo] = useState(new Map())

    const getChatInfo = (id) => {
        axios.get('http://localhost:8080/api/chat/' + id).then(response => {
            let newChatInfo = new Map()
            newChatInfo.set("chatName", response.data.name)
            newChatInfo.set("chatType", response.data.type)
            setChatInfo(newChatInfo)
        });
    }

    const currentChat = (id) => {
        setChat(id)
        getChatInfo(id)
        getConversations()
        scrollToBottom()
    }

    let messagesEnd;

    const scrollToBottom = () => {
        messagesEnd.scrollIntoView({ behavior: "smooth" });
    }

    // Settings

    const [isSettingsModalVisible, setIsSettingsModalVisible] = useState(false);

    const showSettingsModal = () => {
        setIsSettingsModalVisible(true);
    };

    const handleSettingsCancel = () => {
        setIsSettingsModalVisible(false);
    };

    // Create chat

    const [isCreateChatModalVisible, setIsCreateChatModalVisible] = useState(false);

    const showCreateChatModal = () => {
        getNewConversations()
        setIsCreateChatModalVisible(true);
    };

    const handleCreateChatOk = () => {
        setSelected([])
        setIsCreateChatModalVisible(false);
        createNewConversation()
    };

    const handleCreateChatCancel = () => {
        setIsCreateChatModalVisible(false);
    };

    const { Option } = Select;

    const [children, setChildren] = useState([]);

    const getNewConversations = () => {
        const newChildren = [];
        axios.get('http://localhost:8080/api/user/all').then(response => {
            response.data.map(result => {
                newChildren.push(<Option key={result.username}>{result.username}</Option>)
            });
            setChildren([...newChildren])
        });
    }

    const createNewConversation = () => {
        const data = { chatMembersUsername: participants, chatName: chatName };
        axios.post('http://localhost:8080/api/chat/create', data)
            .then(response => {
                setChat(response.data.id)
                getChatInfo(response.data.id)
            });
    }

    const [participants, setParticipants] = useState([]);

    const [open, setOpen] = useState(false);

    function handleChange(value) {
        setParticipants(value);
        setSelected(value);
        setOpen(false)
        if (value.length > 1) {
            setIsChatNameSelectorVisible(false)
        } else {
            setIsChatNameSelectorVisible(true)
        }
    }

    const [isChatNameSelectorVisible, setIsChatNameSelectorVisible] = useState(true);

    const [chatName, setChatName] = useState('Group chat');

    // Chat info

    const [isChatInfoModalVisible, setIsChatInfoModalVisible] = useState(false);

    const showChatInfoModal = () => {
        setIsChatInfoModalVisible(true);
    };

    const handleChatInfoCancel = () => {
        setIsChatInfoModalVisible(false);
    };

    const handleFocus = () => {
        setOpen(true)
    };

    const [selected, setSelected] = useState()

    return (
        <div className="messenger">

            <Modal title="Settings" visible={isSettingsModalVisible} footer={[]} onCancel={handleSettingsCancel}>
                User id: {userId}
                <Button danger type="text">
                    Log out
                </Button>
            </Modal>

            <Modal title="Create chat" visible={isCreateChatModalVisible} onOk={handleCreateChatOk} onCancel={handleCreateChatCancel}>
                Please select users:
                <Select
                    mode="multiple"
                    allowClear
                    style={{ width: '100%' }}
                    placeholder="Start typing username"
                    onChange={handleChange}
                    value={selected}
                    onInputKeyDown={handleFocus}
                    open={open}
                >
                    {children}
                </Select>
                <br/>
                <br/>
                Chat name <i>(group only)</i>:
                <Input placeholder="e.g. New group chat" disabled={isChatNameSelectorVisible} onChange={event => setChatName(event.target.value)} />
            </Modal>

            <Modal title="Chat info" visible={isChatInfoModalVisible} footer={[]} onCancel={handleChatInfoCancel}>
                {chatInfo.get("chatName")}
                <Button danger type="text">
                    Delete chat
                </Button>
            </Modal>

            <div className="scrollable sidebar">
                <div className="conversation-list">
                    <div className="toolbar">
                        <div className="left-items">
                            <Button type="text" size="large" onClick={showSettingsModal} icon={<SettingOutlined style={{ fontSize: '25px', color: '#08c' }} />}>
                            </Button>
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
                            <Button type="text" size="large" onClick={showCreateChatModal} icon={<UsergroupAddOutlined style={{ fontSize: '25px', color: '#08c' }} />}>
                            </Button>
                        </div>
                    </div>

                    <div>
                        {
                            conversations.map(conversation =>
                                <div className="conversation-list-item" onClick={ ()=>currentChat(conversation.id) }>
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
                            <Button type="text" size="large" onClick={showChatInfoModal} icon={<InfoCircleOutlined style={{ fontSize: '25px', color: '#08c' }} />}>
                            </Button>
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