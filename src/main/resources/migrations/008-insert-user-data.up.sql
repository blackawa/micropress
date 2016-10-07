insert into
users (username, nickname, password, email_address, image_url, user_statuses_id)
values
('all privilege holder', 'god', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9'/* admin123 */, 'hoge@micropress.com', null, 1);
--;;
insert into
user_authorities (users_id, authorities_id)
values
(1, 1),
(1, 2),
(1, 3);
