-- name: find-by-username
select * from editor where username = :username

-- name: update-editor!
update editor set
password = :password
where id = :id

-- name: find-all
select * from editor;

-- name: find-by-id
select * from editor where id = :id
