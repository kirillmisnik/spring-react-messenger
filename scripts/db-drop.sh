#!/bin/sh

sudo -u postgres psql << EOF
DROP DATABASE messenger;
DROP USER messenger_admin;
EOF
