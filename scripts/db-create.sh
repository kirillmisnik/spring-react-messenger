#!/bin/sh

sudo -u postgres psql << EOF
create database messenger;
create user messenger_admin with encrypted password 'yEwXUcaNf8cp2RRD';
grant all privileges on database messenger to messenger_admin;
EOF