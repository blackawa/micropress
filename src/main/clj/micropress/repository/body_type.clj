(ns micropress.repository.body-type
  (:require [korma.core :refer [select where]]
            [micropress.entity :as e]))

(defn find-by-id
  [id]
  (first (select e/body-types (where {:id id}))))
