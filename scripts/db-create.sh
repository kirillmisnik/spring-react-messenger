#!/bin/sh

sudo -u postgres psql << EOF
CREATE DATABASE messenger;
CREATE USER messenger_admin WITH ENCRYPTED PASSWORD 'yEwXUcaNf8cp2RRD';
GRANT ALL PRIVILEGES ON DATABASE messenger TO messenger_admin;
EOF
