(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context GET POST]]
            [compojure.route :as route]
            [micropress.service.invite :as i]
            [micropress.util.response :as res]
            [micropress.validator.invite :as vi]))

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req
        [ok? msg] (vi/validate email auth)]
    (if ok?
      (do (i/invite email auth)
          (i/send-invite-mail email)
          (res/created "" {}))
      (res/bad-request {:msg msg}))))

(defn- view-invitees
  [req]
  (i/view-invitees))

(defroutes routes
  (context "/invitation" _
           (GET "/" _ view-invitees)
           (POST "/" _ invite-user)))
