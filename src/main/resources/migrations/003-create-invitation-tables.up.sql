create table invitees (
id int auto_increment not null primary key,
invitation_token varchar(64) not null,
email_address varchar(256) not null,
expire_time timestamp not null
);
--;;
create table invitee_authorities (
invitees_id int not null,
authorities_id int not null,
primary key (invitees_id, authorities_id),
foreign key (invitees_id) references invitees(id) on delete cascade,
foreign key (authorities_id) references authorities(id)
);
