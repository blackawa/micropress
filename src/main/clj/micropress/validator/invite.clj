(ns micropress.validator.invite
  (:require [clj-time.core :as t]
            [micropress.repository :as r]
            [micropress.service.invite :as invite]
            [micropress.util.validator :as v]
            [micropress.validator.auth :as va]
            [micropress.validator.user :as vu]
            [schema.core :as s]))

(def auth-format [s/Num])

(defn- isnt-he-user?
  "すでにユーザーでないかを調べる"
  [email]
  (if (empty? (r/find-user-by-email email))
    [true {:msg nil :target email}]
    [false {:msg (format "%s is already used." email) :target email}]))

(defn valid-invitee-id?
  [invitee-id]
  (let [ok? (not (nil? (r/find-invitee-by-id invitee-id)))]
    (if ok?
      [true {:msg nil :target invitee-id}]
      [false {:msg "Invalid invitation id."} :target invitee-id])))

(defn valid-invitee-token?
  [token]
  (let [ok? (not (nil? (->> (r/find-invitee-by-token token)
                            (filter #(t/before? (t/now) (:expire_time %)))
                            first)))]
    (if ok?
      [true {:msg nil :target token}]
      [false {:msg "Invalid token." :target token}])))

(defn validate-invitation
  [email auth]
  (v/aggregate
   (vu/valid-email? email)
   (v/validate auth-format auth "Invalid Authorities.")
   (isnt-he-user? email)
   (va/valid-auth? auth)))

(defn validate-invitee-id
  [invitee-id]
  (v/aggregate
   (valid-invitee-id? invitee-id)))
