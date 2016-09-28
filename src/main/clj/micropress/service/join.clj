(ns micropress.service.join
  (:require [micropress.repository.invitee :as invitee]
            [micropress.repository.user :as user]
            [micropress.util.encrypt :as ecp]))

(defn accpet-invitation
  [{:keys [token username nickname password]}]
  (let [invitee (first (invitee/find-by-token token))
        invitee-id (:id invitee)
        email (:email_address invitee)
        hashed-pwd (ecp/hash password)
        user (user/insert-user username nickname hashed-pwd email invitee-id)]
    (invitee/delete-by-id invitee-id)
    user))
