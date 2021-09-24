import React, {useEffect, useState} from 'react';
import {Button, Row, Col, Input, Modal, Select, Upload} from "antd";
import axios from "axios";
import {UploadOutlined} from "@ant-design/icons";

export default function CreateChatModal(props) {

    const [chatName, setChatName] = useState('Group chat');
    const [participants, setParticipants] = useState([]);
    const [users, setUsers] = useState([]);
    const [isSelectorVisible, setIsSelectorVisible] = useState(true);
    const [open, setOpen] = useState(false);
    const [selected, setSelected] = useState()

    const {isVisible} = props;

    useEffect(() => {
        getNewConversations()
    },[isVisible]);

    const getNewConversations = () => {
        const newUsers = [];
        axios.get('http://localhost:8080/api/user/all').then(response => {
            response.data.map(result => {
                newUsers.push(<Select.Option key={result.username}>{result.username}</Select.Option>)
            });
            setUsers([...newUsers])
        });
    }

    const createNewConversation = () => {
        const data = { chatMembersUsername: participants, chatName: chatName };
        axios.post('http://localhost:8080/api/chat/create', data)
            .then(response => {
                props.getChatId(response.data.id);
            });
    }

    const handleOk = () => {
        createNewConversation()
        setSelected([])
        props.showModal(false);
    };

    const handleCancel = () => {
        setSelected([])
        props.showModal(false);
    };

    function handleChange(value) {
        setParticipants(value);
        setSelected(value);
        setOpen(false);
        if (value.length > 1) {
            setIsSelectorVisible(false)
        } else {
            setIsSelectorVisible(true)
        }
    }

    const handleFocus = () => {
        setOpen(true)
    };

    return (
        <Modal title="Create chat" visible={isVisible} onOk={handleOk} onCancel={handleCancel}>
            <div>
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
                    {users}
                </Select>
                Chat name and picture <i>(group only)</i>:
                <Row>
                    <Col span={14}>
                        <Input placeholder="e.g. New group chat" disabled={isSelectorVisible} onChange={event => setChatName(event.target.value)} />
                    </Col>
                    <Col span={10}>
                        <Upload>
                            <Button icon={<UploadOutlined />} disabled={isSelectorVisible} >Click to Upload</Button>
                        </Upload>
                    </Col>
                </Row>
            </div>
        </Modal>
    )
}