-- name: create-editor!
insert into editor
(username, password, editor_status_id)
values
(:username, :password, :editor_status_id)

-- name: find-active-by-username
select * from editor
where username = :username and editor_status_id = 1

-- name: update-editor!
update editor set
password = :password
where id = :id

-- name: find-all
select * from editor;

-- name: find-by-id
select * from editor where id = :id
