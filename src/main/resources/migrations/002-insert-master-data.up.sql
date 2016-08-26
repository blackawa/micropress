insert into
user_statuses (user_status_name)
values
('enabled'),
('disabled');
--;;
insert into
authorities (authority_name)
values
('system administrator'),
('editor in chief'),
('editor');
--;;
insert into
users (username, nickname, password, email_address, image_url, user_status_id)
values
('all privilege holder', 'god', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9'/* admin123 */, 'hoge@micropres.com', null, 1);
--;;
insert into
user_authorities (user_id, authority_id)
values
(1, 1),
(1, 2),
(1, 3);
--;;
insert into
events (event_name, description)
values
('publishment', 'Publishing articles. 記事の公開。');
--;;
insert into
article_statuses (article_status_name)
values
('draft'),
('submitted'),
('complete'),
('published'),
('withdrawn');
--;;
insert into
body_types (body_type_name)
values
('Markdown'),
('row HTML');
