import React from 'react';
import './Toolbar.css';

export default function Toolbar(props) {
    const { title, leftItem, rightItem } = props;
    return (
        <div className="toolbar">
            <div className="left-items">{ leftItem }</div>
            <h1 className="toolbar-title">{ title }</h1>
            <div className="right-items">{ rightItem }</div>
        </div>
    );
}