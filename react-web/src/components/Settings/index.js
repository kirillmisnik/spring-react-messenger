import React, {useState} from 'react';
import {Button} from "antd";
import {SettingOutlined} from "@ant-design/icons";
import SettingsModal from "../SettingsModal";

export default function Settings(props) {

    const [isModalVisible, setIsModalVisible] = useState(false);

    const showModal = (isVisible = true) => {
        setIsModalVisible(isVisible);
    };

    return (
        <div>
            <Button type="text" size="large" onClick={showModal} icon={
                <SettingOutlined style={{fontSize: '25px', color: '#08c'}}/>
            } />
            <SettingsModal isVisible={isModalVisible}
                             getChatId={props.getChatId}
                             userId={props.userId}
                             showModal={showModal} />
        </div>
    )
}