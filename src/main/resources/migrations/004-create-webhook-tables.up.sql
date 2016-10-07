create table webhooks (
id int auto_increment not null primary key,
events_id int not null,
url  varchar(256) not null,
foreign key (events_id) references events(id)
);
