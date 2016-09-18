(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.service.invite :as invite]
            [micropress.util.response :as res]
            [micropress.validator.invite :as vi]))

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req
        [ok? msg] (vi/validate email auth)]
    (if ok?
      (do (invite/invite email auth)
          (invite/send-invite-mail email)
          (res/created "" {}))
      (res/bad-request {:msg msg}))))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
