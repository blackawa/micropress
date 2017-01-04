-- name: create-auth-token!
insert into auth_token
(editor_id, token, expire)
values
(:editor_id, :token, :expire)

-- name: find-by-token
select * from auth_token where token = :token
