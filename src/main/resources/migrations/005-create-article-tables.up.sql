create table articles (
id int auto_increment not null primary key,
article_statuses_id int not null,
users_id int not null,
title varchar(256) not null,
thumbnail_url varchar(256),
body_type int not null,
body text not null,
published_at datetime,
foreign key (article_statuses_id) references article_statuses(id),
foreign key (users_id) references users(id)
);
--;;
create table article_histories (
id int auto_increment not null,
articles_id int not null,
article_statuses_id int not null,
users_id int not null,
title varchar(256) not null,
thumbnail_url varchar(256),
body_type int not null,
body text not null,
published_at datetime not null,
updated_at datetime,
primary key (id, articles_id, updated_at),
foreign key (articles_id) references articles(id),
foreign key (article_statuses_id) references article_statuses(id),
foreign key (users_id) references users(id)
);
