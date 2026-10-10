#!/bin/sh
# Starts an in-container MariaDB unless DB_HOST points at a real one (e.g. docker-compose).
# ponytail: DB lives in the app container and its data is lost on redeploy/restart; move to a managed DB when it matters.
set -e

if [ -z "$DB_HOST" ] && [ -z "$DB_URL" ]; then
  mkdir -p /run/mysqld && chown mysql:mysql /run/mysqld
  [ -d /var/lib/mysql/mysql ] || mariadb-install-db --user=mysql --datadir=/var/lib/mysql >/dev/null

  mariadbd --user=mysql --bind-address=127.0.0.1 \
    --innodb-buffer-pool-size=32M --performance-schema=OFF &

  until mariadb-admin ping --silent 2>/dev/null; do sleep 1; done

  mariadb -e "CREATE DATABASE IF NOT EXISTS wms;
    CREATE USER IF NOT EXISTS 'wms'@'%' IDENTIFIED BY 'wms';
    GRANT ALL ON wms.* TO 'wms'@'%';"

  export DB_URL="jdbc:mysql://127.0.0.1:3306/wms?allowPublicKeyRetrieval=true&useSSL=false"
  export DB_USERNAME=wms DB_PASSWORD=wms
fi

exec java -XX:MaxRAMPercentage=60 -jar app.jar
