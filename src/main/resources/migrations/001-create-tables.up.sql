create table user_statuses (
id int auto_increment not null primary key,
user_status_name varchar(128) not null
);

create table user_privileges (
id int auto_increment not null primary key,
privilege_name varchar(128) not null
);

create table users (
id int auto_increment not null primary key,
username varchar(128) not null,
nickname varchar(128) not null,
password varchar(128) not null,
email_address varchar(256) not null,
privilege_id int not null,
image_url varchar(256),
user_status_id int not null,
foreign key (privilege_id) references user_privileges(id),
foreign key (user_status_id) references user_statuses(id)
);

create table user_sessions (
session_uuid varchar(26) not null primary key,
user_id int not null,
expire_time timestamp not null,
foreign key (user_id) references users(id)
);

create table invitations (
id int auto_increment not null primary key,
invitation_token varchar(30) not null,
email_address varchar(256) not null,
expire_time timestamp not null
);

create table user_histories (
id int auto_increment not null,
user_id int not null,
updated_time timestamp not null,
username varchar(128) not null,
nickname varchar(128) not null,
password varchar(128) not null,
email_address varchar(256) not null,
privilege_id int not null,
image_url varchar(256),
user_status_id int not null,
foreign key (user_id) references users(id)
foreign key (privilege_id) references user_privileges(id),
foreign key (user_status_id) references user_statuses(id),
primary key (id, user_id, updated_time)
);

create table events (
id int auto_increment not null primary key,
event_name varchar(128) not null,
description varchar(128)
);

create table webhooks (
id int auto_increment not null primary key,
event_id int not null,
url  varchar(256) not null,
foreign key (event_id) references events(id)
);

create table article_statuses (
id int auto_increment not null primary key,
article_status_name varchar(128) not null
);

create table body_types (
id int auto_increment not null primary key,
body_type_name varchar(128) not null
);

create table articles (
id int auto_increment not null primary key,
article_status_id int not null,
user_id int not null,
title varchar(256) not null,
thumbnail_url(256),
description text not null,
body_type int not null,
body text not null,
foreign key (user_id) references users(id)
);

create table article_status_histories (
id int auto_increment not null,
article_id int not null,
updated_time timestamp not null,
atricle_status_id int not null,
primary key (id, article_id, updated_time)
);
