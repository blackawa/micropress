(ns micropress.repository.article
  (:require [clj-time.jdbc]
            [korma.core :refer [insert values
                                   select
                                   update set-fields
                                   delete
                                   where with]]
            [micropress.entity :as e]))

(defn find-draft-by-id
  [article-id user-id]
  (first (select e/articles
                 (with e/article-statuses)
                 (where {:id article-id
                         :users_id user-id}))))

(defn find-all-drafts
  [user-id]
  (select e/articles
          (with e/article-statuses)
          (where {:users_id user-id})))

(defn insert-article
  [title body thumbnail-url body-type user-id]
  (insert e/articles
          (values {:article_statuses_id 1
                   :users_id user-id
                   :title title
                   :thumbnail_url thumbnail-url
                   :body_type body-type
                   :body body})))

(defn find-by-id
  [article-id]
  (first (select e/articles
                 (where {:id article-id}))))

(defn submit-draft
  [article-id]
  (update e/articles
          (set-fields {:article_statuses_id 2})
          (where {:id article-id})))

(defn publish-article
  [article-id publish-at]
  (update e/articles
          (set-fields {:article_statuses_id 4
                       :published_at publish-at})))

(defn update-draft
  [article-id title body thumbnail-url body-type submit? user-id]
  (update e/articles
          (set-fields {:title title
                       :body body
                       :thumbnail_url thumbnail-url
                       :body_type body-type
                       :users_id user-id
                       :article_statuses_id (if submit? 2 1)})
          (where {:id article-id})))

(defn delete-article
  [article-id]
  (delete e/articles (where {:id article-id})))
