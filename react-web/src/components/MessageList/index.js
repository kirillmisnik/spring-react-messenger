import React, {useEffect, useState} from "react";
import axios from "axios";
import moment from "moment";
import Index from "../Message";
import useInterval from "../../UseInterval";
import Compose from "../Compose";

import './MessageList.css';

export default function MessageList(props) {

    const [messages, setMessages] = useState([])
    const {userId, chatId} = props

    useEffect(() => {
        getMessages()
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

    return (
        <div className="message-list">
            <div className="message-list-container">{renderMessages()}</div>
            <Compose chatId={chatId}/>
        </div>
    )
}