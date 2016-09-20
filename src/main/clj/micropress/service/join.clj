(ns micropress.service.join
  (:require [clj-time.core :as c]
            [clj-time.format :as f]
            [micropress.repository :as r]
            [micropress.util.encrypt :as ecp]))

(defn- migrate-auth
  "権限情報を招待者からユーザーに移し替える"
  [invitee-id user-id]
  (r/find-invitee-auth-by-id invitee-id))

(defn accpet-invitation
  [{:keys [token username nickname password]}]
  (let [invitee (first (r/find-invitee-by-token token))
        invitee-id (:id invitee)
        email (:email_address invitee)
        hashed-pwd (ecp/hash password)
        user (r/insert-user username nickname hashed-pwd email invitee-id)]
    (r/delete-invitee invitee-id)
    user))
