(ns micropress.repository
  (:import [java.sql SQLException])
  (:require [clj-time.core :as c]
            [clj-time.jdbc]
            [korma.core :refer [insert values select where fields delete with]]
            [micropress.entity :as e]))

(defn delete-session
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

(defn find-session
  [token]
  (select e/user-sessions
          (where {:token token})))

(defn insert-invitee
  [token email auth]
  (let [expire-time (c/plus (c/now) (c/days 1))
        invitee-id (:generated_key (insert e/invitees (values {:invitation_token token :email_address email :expire_time expire-time})))]
    (insert e/invitee-authorities
            (values (map (fn [authorities-id] {:invitees_id invitee-id :authorities_id authorities-id}) auth)))
    {:ok? true :invitee-id invitee-id}))

(defn find-all-invitees
  []
  (select e/invitees
          (fields :id :email_address :expire_time)))

(defn find-invitee-by-token
  [token]
  (->> (select e/invitees
               (where {:invitation_token token}))))

(defn find-user-by-email
  [email]
  (select e/users
          (where {:email_address email :user_statuses_id 1})))

(defn find-auth-by-id
  [auth-id]
  (first (select e/authorities (where {:id auth-id}))))

(defn find-invitee-by-id
  [invitee-id]
  (first (select e/invitees (where {:id invitee-id}))))

(defn delete-invitee
  [invitee-id]
  (delete e/invitees (where {:id invitee-id})))

(defn insert-user
  "ユーザー情報と権限を登録する."
  [username nickname password email invitee-id]
  (let [user-id (:generated_key (insert e/users (values {:username username :nickname nickname :password password :email_address email
                                                         :user_statuses_id 1})))
        auths (map (fn [m] {:users_id user-id :authorities_id (:authorities_id m)})
                   (select e/invitee-authorities (where {:invitees_id invitee-id})))]
    (insert e/user-authorities (values auths))
    {:user-id user-id}))

(defn find-invitee-auth-by-id
  [invitee-id]
  (select e/invitee-authorities (where {:invitees_id invitee-id})))

(defn find-users
  []
  (select e/users (with e/authorities)))

(defn find-user
  [user-id]
  (select e/users
          (with e/authorities)
          (where {:id user-id})))

(defn find-user-status-by-id
  [user-status-id]
  (select e/user-statuses (where {:id user-status-id})))
