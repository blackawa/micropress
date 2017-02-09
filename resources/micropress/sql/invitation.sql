-- name: create-invitation<!
insert into invitation
(token, invitation_status_id)
values
(:token, :invitation_status_id)

-- name: find-by-token
select * from invitation where token = :token
