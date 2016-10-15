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
foreign key (users_id) references users(id) on delete cascade,
foreign key (authorities_id) references authorities(id)
);
--;;
create table user_sessions (
token varchar(64) not null primary key,
users_id int not null,
content blob,
expire_time datetime not null,
foreign key (users_id) references users(id) on delete cascade
);
--;;
create table user_histories (
id int auto_increment not null,
users_id int not null,
updated_at datetime not null,
username varchar(128) not null,
nickname varchar(128) not null,
password varchar(128) not null,
email_address varchar(256) not null,
image_url varchar(256),
user_statuses_id int not null,
foreign key (users_id) references users(id),
foreign key (user_statuses_id) references user_statuses(id),
primary key (id, users_id, updated_at)
);
