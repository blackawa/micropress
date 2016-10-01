(ns micropress.repository.session
  (:import [java.sql SQLException])
  (:require [clj-time.core :as c]
            [clj-time.jdbc]
            [korma.core :refer [insert values
                                select with
                                delete
                                where]]
            [micropress.entity :as e]))

(defn delete-by-user-id
  [user-id]
  (try (delete e/user-sessions
                 (where {:users_id user-id}))
         {:ok? true :message nil}
         (catch SQLException e {:ok? false :message (.getMessage e)})))

(defn insert-session
  [id token]
  (let [expire-time (c/plus (c/now) (c/minutes 30))]
    (try (insert e/user-sessions
                 (values {:users_id id :token token :expire_time expire-time}))
         {:ok? true :message nil}
         (catch SQLException e {:ok? false :message (.getMessage e)}))))

(defn find-by-token
  [token]
  (select e/user-sessions
          (where {:token token})))

(defn find-by-token-with-user
  [token]
  (first (select e/user-sessions
                 (with e/users (with e/authorities))
                 (where {:token token}))))
