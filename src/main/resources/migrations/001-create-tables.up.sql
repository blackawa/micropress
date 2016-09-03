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
create table users (
id int auto_increment not null primary key,
username varchar(128) not null,
nickname varchar(128) not null,
password varchar(128) not null,
email_address varchar(256) not null,
image_url varchar(256),
user_statuses_id int not null,
foreign key (user_statuses_id) references user_statuses(id)
);
--;;
create table user_authorities (
users_id int not null,
authorities_id int not null,
primary key (users_id, authorities_id),
foreign key (users_id) references users(id),
foreign key (authorities_id) references authorities(id)
);
--;;
create table user_sessions (
token varchar(64) not null primary key,
users_id int not null,
content blob,
expire_time timestamp not null,
foreign key (users_id) references users(id)
);
--;;
create table invitations (
id int auto_increment not null primary key,
invitation_token varchar(30) not null,
email_address varchar(256) not null,
expire_time timestamp not null
);
--;;
create table user_histories (
id int auto_increment not null,
user_id int not null,
updated_time timestamp not null,
username varchar(128) not null,
nickname varchar(128) not null,
password varchar(128) not null,
email_address varchar(256) not null,
image_url varchar(256),
user_statuses_id int not null,
foreign key (user_statuses_id) references user_statuses(id),
primary key (id, user_id, updated_time)
);
--;;
create table events (
id int auto_increment not null primary key,
event_name varchar(128) not null,
description varchar(128)
);
--;;
create table webhooks (
id int auto_increment not null primary key,
events_id int not null,
url  varchar(256) not null,
foreign key (events_id) references events(id)
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
--;;
create table articles (
id int auto_increment not null primary key,
article_statuses_id int not null,
users_id int not null,
title varchar(256) not null,
thumbnail_url varchar(256),
description text not null,
body_type int not null,
body text not null,
foreign key (article_statuses_id) references article_statuses(id),
foreign key (users_id) references users(id)
);
--;;
create table article_status_histories (
id int auto_increment not null,
article_id int not null,
updated_time timestamp not null,
atricle_statuses_id int not null,
primary key (id, article_id, updated_time)
);
