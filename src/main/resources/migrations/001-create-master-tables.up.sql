create table user_statuses (
id int auto_increment not null primary key,
user_status_name varchar(128) not null
);
--;;
create table authorities (
id int auto_increment not null primary key,
authority_name varchar(128) not null
);
--;;

create table events (
id int auto_increment not null primary key,
event_name varchar(128) not null,
description varchar(128)
);
--;;
create table article_statuses (
id int auto_increment not null primary key,
article_status_name varchar(128) not null
);
--;;
create table body_types (
id int auto_increment not null primary key,
body_type_name varchar(128) not null
);
