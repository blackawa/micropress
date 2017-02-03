delete from article;
--;;
alter sequence article_id_seq restart;
--;;
delete from auth_token;
--;;
delete from editor;
--;;
alter sequence editor_id_seq restart;
