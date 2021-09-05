import React from 'react';
import moment from 'moment';

import './Message.css';

export default function Message(props) {
    const {
        data,
        chatType,
        senderName,
        isMine,
        startsSequence,
        endsSequence,
        showTimestamp
    } = props;

    const showSenderName = () => {
        if (chatType === "GROUP" && !isMine) {
            return senderName
        }
    }

    const friendlyTimestamp = moment(data.timestamp).format('LLLL');
    return (
      <div className={[
        'message',
        `${isMine ? 'mine' : ''}`,
        `${startsSequence ? 'start' : ''}`,
        `${endsSequence ? 'end' : ''}`
      ].join(' ')}>
        {
          showTimestamp &&
            <div className="timestamp">
              { friendlyTimestamp }
            </div>
        }

        <div className="bubble-container">
            { showSenderName() }
          <div className="bubble" title={friendlyTimestamp}>
            { data.text }
          </div>
        </div>
      </div>
    );
}