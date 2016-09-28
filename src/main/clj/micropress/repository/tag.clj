(ns micropress.repository.tag
  (:require [korma.core :refer [select where]]
            [micropress.entity :as e]))

(defn find-by-id
  [id]
  (first (select e/tags (where {:id id}))))
