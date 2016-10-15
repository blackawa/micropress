(ns micropress.repository.user
  (:require [korma.core :refer [insert values
                                select fields
                                delete
                                update set-fields
                                where with]]
            [micropress.constant.code :as c]
            [micropress.entity :as e]))

(defn find-by-email
  [email]
  (select e/users
          (where {:email_address email :user_statuses_id c/user-status-enabled})))

(defn insert-user
  "ユーザー情報と権限を登録する."
  [username nickname password email invitee-id]
  (let [user-id (:generated_key (insert e/users (values {:username username :nickname nickname :password password :email_address email
                                                         :user_statuses_id c/user-status-enabled})))
        auths (map (fn [m] {:users_id user-id :authorities_id (:authorities_id m)})
                   (select e/invitee-authorities (where {:invitees_id invitee-id})))]
    (insert e/user-authorities (values auths))
    {:user-id user-id}))

(defn find-all
  []
  (select e/users (with e/authorities)))

(defn find-by-id
  [user-id]
  (select e/users
          (with e/authorities)
          (where {:id user-id})))

(defn find-active
  [user-id]
  (select e/users
          (with e/authorities)
          (where {:id user-id :user_statuses_id 1})))

(defn find-active-by-email
  [email]
  (first (select e/users
                 (fields :id :email_address :password)
                 (where {:email_address email
                         :user_statuses_id 1}))))

(defn update-user
  "与えられたユーザー情報と権限でユーザーを更新する."
  [{:keys [user-id username nickname password email image-url user-status-id auth] :as params}]
  (let [auths (map (fn [a] {:users_id user-id :authorities_id a}) auth)]
    (update e/users (set-fields {:username username
                                 :nickname nickname
                                 :password password
                                 :email_address email
                                 :image_url image-url
                                 :user_statuses_id user-status-id})
            (where {:id user-id}))
    (delete e/user-authorities (where {:users_id user-id}))
    (insert e/user-authorities (values auths))))
