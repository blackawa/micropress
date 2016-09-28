(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context GET POST DELETE]]
            [micropress.service.invite :as i]
            [micropress.util.response :as res]
            [micropress.validator.invite :as vi]))

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req
        {:keys [ok? messages]} (vi/validate-invitation email auth)]
    (if ok?
      (do (i/invite email auth)
          (i/send-invite-mail email)
          (res/created "" {}))
      (res/bad-request messages))))

(defn- view-invitees
  [req]
  (res/ok (i/view-invitees)))

(defn- withdraw-invitation
  [req]
  (let [invitee-id (-> req :params :invitee-id)
        {:keys [ok? messages]} (vi/validate-invitee-id invitee-id)]
    (if ok?
      (do (i/delete-invitation invitee-id)
          (res/ok))
      (res/bad-request messages))))

(defroutes routes
  (context "/invitation" _
           (GET "/" _ view-invitees)
           (POST "/" _ invite-user)
           (DELETE "/:invitee-id" _ withdraw-invitation)))
