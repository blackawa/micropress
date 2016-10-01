(ns micropress.repository.auth
  (:require [korma.core :refer [select where]]
            [micropress.entity :as e]))

(defn find-by-id
  [auth-id]
  (first (select e/authorities (where {:id auth-id}))))
