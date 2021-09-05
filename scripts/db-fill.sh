#!/bin/sh

psql -U postgres -d messenger -a -f db-data.sql
