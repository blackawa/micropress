(ns micropress.service.authorization
  (:require [korma.core :refer [select where with fields]]
            [micropress.entity :as e]))

(defn find-by-token
  [token]
  (:authorities (first (select e/user-sessions
                               (with e/users (with e/authorities))
                               (where {:token token})))))
