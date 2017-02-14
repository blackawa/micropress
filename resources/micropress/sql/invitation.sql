-- name: create-invitation<!
insert into invitation
(token, invitation_status_id)
values
(:token, :invitation_status_id)

-- name: find-by-token
select * from invitation where token = :token and invitation_status_id = 1

-- name: accept!
update invitation set invitation_status_id = 2 where id = :id
