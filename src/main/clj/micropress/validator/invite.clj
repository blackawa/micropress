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
  [email target]
  (let [ok? (empty? (r/find-user-by-email email))]
    (v/->result ok? (when (not ok?) (format "%s is already used." email)) target)))

(defn valid-invitee-id?
  [invitee-id target]
  (let [ok? (not (nil? (r/find-invitee-by-id invitee-id)))]
    (v/->result ok? (when (not ok?) "Invalid invitation id.") target)))

(defn valid-invitee-token?
  [token target]
  (let [ok? (not (nil? (->> (r/find-invitee-by-token token)
                            (filter #(t/before? (t/now) (:expire_time %)))
                            first)))]
    (v/->result ok? (when (not ok?) "Invalid token.") target)))

(defn validate-invitation
  [email auth]
  (v/aggregate
   (vu/valid-email? email :email)
   (v/validate auth-format auth :auth "Invalid Authorities.")
   (isnt-he-user? email :email)
   (va/valid-auth? auth :auth)))

(defn validate-invitee-id
  [invitee-id]
  (v/aggregate
   (valid-invitee-id? invitee-id :invitee-id)))
