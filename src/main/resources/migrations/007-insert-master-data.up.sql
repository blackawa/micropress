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
--;;
insert into
events (event_name, description)
values
('publishment', 'Publishing articles. 記事の公開。');
