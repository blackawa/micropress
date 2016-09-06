(ns micropress.repository
  (:import [java.sql SQLException])
  (:require [clj-time.core :as c]
            [clj-time.jdbc]
            [korma.core :refer [insert values select where order delete]]
            [micropress.entity :as e]))

(defn delete-session
  [user-id]
  (try (delete e/user-sessions
                 (where {:users_id user-id}))
         {:ok? true :message nil}
         (catch SQLException e {:ok? false :message (.getMessage e)})))

(defn insert-session
  [id token]
  (let [now (c/now)
        expire-time (c/plus (c/now) (c/minutes 30))]
    (try (insert e/user-sessions
                 (values {:users_id id :token token :expire_time expire-time}))
         {:ok? true :message nil}
         (catch SQLException e {:ok? false :message (.getMessage e)}))))

(defn find-session
  [token]
  (select e/user-sessions
          (where {:token token})))
