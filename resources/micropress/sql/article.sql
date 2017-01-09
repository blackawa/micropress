-- name: find-all
select * from article order by id

-- name: find-all-published
select * from article where article_status_id = 2 order by published_date desc

-- name: find-published-by-id
select * from article where id = :id and article_status_id = 2

-- name: create-article<!
insert into article
(title, content, editor_id, article_status_id, published_date)
values
(:title, :content, :editor_id, :article_status_id, :published_date)

-- name: find-by-id
select * from article where id = :id

-- name: update-article!
update article
set title = :title,
    content = :content,
    editor_id = :editor_id,
    article_status_id = :article_status_id,
    published_date = :published_date
where id = :id
