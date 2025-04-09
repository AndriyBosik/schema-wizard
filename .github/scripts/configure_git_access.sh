#!/bin/bash

set -e

ssh_key=$1

mkdir -p ~/.ssh
echo "$ssh_key" > ~/.ssh/id_rsa
chmod +x ~/.ssh/id_rsa
ssh-keyscan github.com >> ~/.ssh/known_hosts
