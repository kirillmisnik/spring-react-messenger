import React, {useEffect, useState} from 'react';
import {Button, Row, Col, Input, Modal, Select, Upload} from "antd";
import axios from "axios";
import {UploadOutlined} from "@ant-design/icons";

export default function SettingsModal(props) {

    const {isVisible} = props;

    const username = "Kirill Misnik";

    const handleOk = () => {
        props.showModal(false);
    };

    const handleCancel = () => {
        props.showModal(false);
    };

    const logout = () => {
        axios({
            method: 'get',
            url: 'http://localhost:8080/login?logout',
        })
    }

    const deleteAccount = (e) => {
        e.preventDefault();
        axios({
            method: 'delete',
            url: 'http://localhost:8080/api/user/' + props.userId,
        })
        logout()
    }

    return (
        <Modal title="Settings" visible={isVisible} onOk={handleOk} onCancel={handleCancel} footer={[]}>
            <div>
                <h2>{username}</h2>
                <Button danger type="text" onClick={logout}>
                    Log out
                </Button>
                <br/>
                <Button danger type="text" onClick={deleteAccount}>
                    Delete account
                </Button>
            </div>
        </Modal>
    )
}