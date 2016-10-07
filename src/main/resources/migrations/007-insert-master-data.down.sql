/* Pre Execution */
delete from webhooks;
--;;
alter table webhooks auto_increment = 1;
--;;

/* Target SQL */
delete from user_statuses;
--;;
alter table user_statuses auto_increment = 1;
--;;
delete from authorities;
--;;
alter table authorities auto_increment = 1;
--;;
delete from article_statuses;
--;;
alter table article_statuses auto_increment = 1;
--;;
delete from body_types;
--;;
alter table body_types auto_increment = 1;
--;;
delete from events;
--;;
alter table events auto_increment = 1;
