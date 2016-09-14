(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.service.invite :as invite]
            [micropress.util.response :as res]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(def ^:private auth-format [s/Num])

(defn- unique-invitee?
  "すでに招待済みでないかを調べる"
  [email]
  [true {:msg nil :target email}]) ;;todo implement me

(defn- isnt-he-user?
  "すでにユーザーでないかを調べる"
  [email]
  [true {:msg nil :target email}]) ;;todo implement me

(defn- valid-auth?
  "権限IDが正しいか調べる"
  [auth]
  [true {:msg nil :target auth}]) ;;todo implement me

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req
        [ok? msg] (v/aggregate
                   (v/validate v/email-format email "Invalid Email Address")
                   (v/validate auth-format auth "Invalid Authorities")
                   (unique-invitee? email)
                   (isnt-he-user? email)
                   (valid-auth? auth))]
    (if ok?
      (do (invite/invite email auth)
          (invite/send-invite-mail email)
          (res/created "" {}))
      (res/bad-request {:msg msg}))))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
