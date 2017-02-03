create table invitation_status (
  id integer primary key,
  description text
);
--;;
create table invitation (
  id serial primary key,
  token text not null,
  invitation_status_id integer not null,
  foreign key (invitation_status_id) references invitation_status (id)
);
