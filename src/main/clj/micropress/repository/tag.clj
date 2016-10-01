(ns micropress.repository.tag
  (:require [korma.core :refer [select where
                                insert values]]
            [micropress.entity :as e]))

(defn find-by-id
  [id]
  (first (select e/tags (where {:id id}))))

(defn insert-tags
  "複数タグの挿入を行う.
   戻り値のgenerated_keyはアテにしないこと."
  [tags]
  (->> tags
       (map (fn [tag-name]
              (insert e/tags (values {:tag_name tag-name}))))
       (map #(:generated_key %))))
