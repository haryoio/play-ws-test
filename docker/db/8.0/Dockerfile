FROM mysql/mysql-server:8.0.32

COPY init_db.sql /docker-entrypoint-initdb.d/
COPY my.cnf /usr/etc/my.cnf

COPY 8.0/init_db.8.0.sql /docker-entrypoint-initdb.d/
COPY 8.0/my.8.0.cnf .

ENV MYSQL_ALLOW_EMPTY_PASSWORD=true

# TZの設定
RUN echo 'Asia/Tokyo' > /etc/timezone && cp /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

CMD ["mysqld", "--defaults-extra-file=my.8.0.cnf"]