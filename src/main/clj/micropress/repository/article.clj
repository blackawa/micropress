(ns micropress.repository.article
  (:require [korma.core :refer [insert values]]
            [micropress.entity :as e]))

(defn insert-article
  [title body thumbnail-url body-type user-id]
  (insert e/articles
          (values {:article_statuses_id 2
                   :users_id user-id
                   :title title
                   :thumbnail_url thumbnail-url
                   :body_type body-type
                   :body body})))
