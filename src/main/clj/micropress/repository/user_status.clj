(ns micropress.repository.user-status
  (:require [korma.core :refer [select where]]
            [micropress.entity :as e]))

(defn find-by-id
  [user-status-id]
  (select e/user-statuses (where {:id user-status-id})))
