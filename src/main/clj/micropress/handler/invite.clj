(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.service.invite :as invite]
            [micropress.util.response :as res]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(def ^:private invite-user-req-validator
  {:params {:email v/email-format :auth [s/Num]}})

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req]
    (invite/invite email auth)
    (invite/send-invite-mail email)
    (res/created "" "")))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
