(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.service.invite :as invite]
            [micropress.util.response :as res]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(def ^:private auth-format [s/Num])

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req
        [ok? res] (v/validate {:email v/email-format :auth auth-format} {:email email :auth auth})]
    (if ok?
      (do (invite/invite email auth)
          (invite/send-invite-mail email)
          (res/created "" {}))
      (res/bad-request res))))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
