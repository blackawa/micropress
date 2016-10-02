(ns micropress.util.context)

(defn get-user-id
  [req]
  (get-in req [:context :user-id]))
