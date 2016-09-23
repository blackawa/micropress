(ns micropress.service.user
  (:require [clj-time.core :as c]
            [clj-time.format :as f]
            [micropress.repository :as r]
            [micropress.util.encrypt :as ecp]
            [micropress.util.time :as t]))

(defn view-users
  "招待一覧を返す.
   ただし返却する情報からパスワードなどの情報は抜く."
  []
  (->> (r/find-users)
       (map (fn [{:keys [id username nickname email_address image_url user_statuses_id authorities]}]
              {:id id
               :username username
               :nickname nickname
               :email_address email_address
               :image_url image_url
               :user_statuses_id user_statuses_id
               :authorities authorities}))))

(defn view-user
  [user-id]
  (->> (r/find-user user-id)
       (map (fn [{:keys [id username nickname email_address image_url user_statuses_id authorities]}]
              {:id id
               :username username
               :nickname nickname
               :email_address email_address
               :image_url image_url
               :user_statuses_id user_statuses_id
               :authorities authorities}))
       first))

(defn update-user
  [{:keys [user-id username nickname password email image-url user-status-id auth] :as params}]
  (r/update-user (assoc params :password (ecp/hash password))))
