create table tags (
id int auto_increment not null primary key,
tag_name varchar(128) not null
);
--;;
create table article_tags (
articles_id int not null,
tags_id int not null,
primary key (articles_id, tags_id),
foreign key (articles_id) references articles(id) on delete cascade,
foreign key (tags_id) references tags(id) on delete cascade
);
