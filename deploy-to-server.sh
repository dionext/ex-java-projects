#!/bin/bash

ssh -t root@$REMOTE_SERVER_ADDRESS << EOF
mkdir -p /mnt/$APP_NAME/app/target
EOF

scp $APP_NAME/target/*.jar root@$REMOTE_SERVER_ADDRESS:/mnt/$APP_NAME/app/target/java-app.jar
scp Dockerfile root@$REMOTE_SERVER_ADDRESS:/mnt/$APP_NAME/app/Dockerfile
scp docker-compose.yml root@$REMOTE_SERVER_ADDRESS:/mnt/$APP_NAME/docker-compose.yml
scp $APP_NAME/.env root@$REMOTE_SERVER_ADDRESS:/mnt/$APP_NAME/.env

ssh -t root@$REMOTE_SERVER_ADDRESS << EOF
cd /mnt/$APP_NAME
docker-compose build
docker-compose down
docker-compose up -d
EOF



