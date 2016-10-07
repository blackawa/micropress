/* Pre Execution */
delete from article_histories;
--;;
alter table article_histories auto_increment = 1;
--;;
delete from articles;
--;;
alter table articles auto_increment = 1;
--;;
delete from user_histories;
--;;
alter table user_histories auto_increment = 1;
--;;

/* Execution */
delete from user_authorities;
--;;
alter table user_authorities auto_increment = 1;
--;;
delete from users;
--;;
alter table users auto_increment = 1;
