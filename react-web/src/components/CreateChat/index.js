import React, {useState} from 'react';
import {Button} from "antd";
import {UsergroupAddOutlined} from "@ant-design/icons";
import CreateChatModal from "../CreateChatModal";

export default function CreateChat(props) {

    const [isModalVisible, setIsModalVisible] = useState(false);

    const showModal = (isVisible = true) => {
        setIsModalVisible(isVisible);
    };

    return (
        <div>
            <Button type="text" size="large" onClick={showModal} icon={
                <UsergroupAddOutlined style={{fontSize: '25px', color: '#08c'}}/>
            } />
            <CreateChatModal isVisible={isModalVisible}
                             getChatId={props.getChatId}
                             showModal={showModal} />
        </div>
    )
}