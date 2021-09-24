import React, {useEffect, useState} from 'react';
import {Button, Row, Col, Input, Modal, Select, Upload} from "antd";
import axios from "axios";
import {UploadOutlined} from "@ant-design/icons";

export default function ChatInfoModal(props) {

    const {isVisible} = props;

    const handleOk = () => {
        props.showModal(false);
    };

    const handleCancel = () => {
        props.showModal(false);
    };

    const deleteChat = (e) => {
        e.preventDefault();
        axios({
            method: 'delete',
            url: 'http://localhost:8080/api/chat/' + props.chatInfo.get("chatId"),
        })
    }

    return (
        <Modal title="Settings" visible={isVisible} onOk={handleOk} onCancel={handleCancel} footer={[]}>
            <div>
                <h2>{props.chatInfo.get("chatName")}</h2>
                <Button danger type="text" onClick={deleteChat}>
                    Delete chat
                </Button>
            </div>
        </Modal>
    )
}