(ns micropress.repository.article-tag
  (:require [korma.core :refer [insert values
                                delete
                                where]]
            [micropress.entity :as e]))

(defn insert-article-tags
  [article-id tag-ids]
  (insert e/article-tags
          (values (map (fn [tag-id] {:articles_id article-id
                                     :tags_id tag-id}) tag-ids))))

(defn delete-by-article-id
  [article-id]
  (delete e/article-tags
          (where {:articles_id article-id})))
