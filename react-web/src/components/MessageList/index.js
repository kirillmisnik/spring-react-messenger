import React, {useEffect, useState} from "react";
import axios from "axios";
import moment from "moment";
import Index from "../Message";
import useInterval from "../../UseInterval";
import Compose from "../Compose";

import './MessageList.css';
import Toolbar from "../Toolbar";
import Settings from "../Settings";
import CreateChat from "../CreateChat";
import ChatInfo from "../ChatInfo";

export default function MessageList(props) {

    const [messages, setMessages] = useState([])
    const [chatInfo, setChatInfo] = useState(new Map())
    const {userId, chatId} = props

    useEffect(() => {
        getMessages()
        getChatInfo()
    },[chatId])

    useInterval(() => {
        getMessages()
    }, 2000)

    const getMessages = () => {
        axios.get('http://localhost:8080/api/chat/' + chatId + '/messages/all').then(response => {
            let newMessages = response.data.map(result => {
                return {
                    id: result.id,
                    senderId: result.senderId,
                    text: result.text,
                    creationDate: result.creationDate
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
            let isMine = current.senderId === userId;
            let currentMoment = moment(current.creationDate);
            let prevBySameAuthor = false;
            let nextBySameAuthor = false;
            let startsSequence = true;
            let endsSequence = true;
            let showTimestamp = true;

            if (previous) {
                let previousMoment = moment(previous.creationDate);
                let previousDuration = moment.duration(currentMoment.diff(previousMoment));
                prevBySameAuthor = previous.senderId === current.senderId;

                if (prevBySameAuthor && previousDuration.as('hours') < 1) {
                    startsSequence = false;
                }

                if (previousDuration.as('hours') < 1) {
                    showTimestamp = false;
                }
            }

            if (next) {
                let nextMoment = moment(next.creationDate);
                let nextDuration = moment.duration(nextMoment.diff(currentMoment));
                nextBySameAuthor = next.senderId === current.senderId;

                if (nextBySameAuthor && nextDuration.as('hours') < 1) {
                    endsSequence = false;
                }
            }

            tempMessages.push(
                <Index
                    key={i}
                    // chatType={chatInfo.get("chatType")}
                    senderName={current.senderId}
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

    const getChatInfo = () => {
        axios.get('http://localhost:8080/api/chat/' + chatId).then(response => {
            let newChatInfo = new Map()
            newChatInfo.set("chatId", response.data.id)
            newChatInfo.set("chatName", response.data.name)
            newChatInfo.set("chatType", response.data.type)
            setChatInfo(newChatInfo)
        });
    }

    return (
        <div className="message-list">
            <Toolbar
                title={chatInfo.get("chatName")}
                rightItem={<ChatInfo getChatId={props.getChatId}
                                     chatInfo={chatInfo} />}
            />
            <div className="message-list-container">{renderMessages()}</div>
            <Compose chatId={chatId}/>
        </div>
    )
}