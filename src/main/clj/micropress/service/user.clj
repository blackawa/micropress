(ns micropress.service.user
  (:require [micropress.repository.user :as user]
            [micropress.util.encrypt :as ecp]))

(defn view-users
  "招待一覧を返す.
   ただし返却する情報からパスワードなどの情報は抜く."
  []
  (->> (user/find-all)
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
  (->> (user/find-by-id user-id)
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
  (user/update-user (assoc params :password (ecp/hash password))))
