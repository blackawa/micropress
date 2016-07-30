#!/bin/bash

echo "================"
echo "this operation will DELETE existing docker container. ok? [Press Enter for yes]"
echo "================"
read ANSWER

docker stop micropress-db
docker rm micropress-db
echo "================"
echo "creating docker container for our micropress db..."
echo "================"
docker run --name micropress-db -d -e MYSQL_ROOT_PASSWORD=p@ssw0rd -p 3306:3306 mysql:latest
echo "================"
echo "created."
echo "Waiting for DB to start up..."
echo "================"
docker exec micropress-db mysqladmin --silent --wait=30 -uroot -pp@ssw0rd ping || exit 1
echo "================"
echo "done."
echo "creating database..."
echo "================"
docker exec -i micropress-db mysql -uroot -pp@ssw0rd < create-db.sql
echo "================"
echo "done."
echo "creating user..."
echo "================"
docker exec -i micropress-db mysql -uroot -pp@ssw0rd < create-user.sql
echo "================"
echo "done."
echo "creating tables..."
echo "================"
docker exec -i micropress-db mysql -uroot -pp@ssw0rd -D micropress < migrate.sql
echo "================"
echo "done."
echo "================"
