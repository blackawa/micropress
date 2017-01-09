create table editor_status (
  id serial primary key,
  description text not null
);
--;;
create table editor (
  id serial primary key,
  username varchar(32) not null,
  password varchar(98) not null,
  editor_status_id integer not null,
  foreign key (editor_status_id) references editor_status (id)
);
