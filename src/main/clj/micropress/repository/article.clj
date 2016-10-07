(ns micropress.repository.article
  (:require [korma.core :refer [insert values
                                select
                                update set-fields
                                where]]
            [micropress.entity :as e]))

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
