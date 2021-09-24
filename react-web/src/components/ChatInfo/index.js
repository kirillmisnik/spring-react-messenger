import React, {useState} from 'react';
import {Button} from "antd";
import {InfoCircleOutlined} from "@ant-design/icons";
import CreateChatModal from "../CreateChatModal";
import ChatInfoModal from "../ChatInfoModal";

export default function ChatInfo(props) {

    const [isModalVisible, setIsModalVisible] = useState(false);

    const showModal = (isVisible = true) => {
        setIsModalVisible(isVisible);
    };

    return (
        <div>
            <Button type="text" size="large" onClick={showModal} icon={
                <InfoCircleOutlined style={{fontSize: '25px', color: '#08c'}}/>
            } />
            <ChatInfoModal   isVisible={isModalVisible}
                             getChatId={props.getChatId}
                             chatInfo={props.chatInfo}
                             showModal={showModal} />
        </div>
    )
}