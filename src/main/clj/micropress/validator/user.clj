(ns micropress.validator.user
  (:require [clj-time.core :as t]
            [micropress.repository :as r]
            [micropress.service.invite :as invite]
            [micropress.util.validator :as v]
            [micropress.validator.auth :as va]
            [schema.core :as s]))

(defn valid-username?
  [username target]
  (let [un-str-type? (v/validate v/username username target)
        un-length-ok? (< (count username) 128)
        ok? (and (:ok? un-str-type?) un-length-ok?)]
    (v/->result ok? (when (not ok?) "Username should be less than 128, and can contain a-z, A-Z, 0-9, '-' and '_'.") target)))

(defn valid-nickname?
  [nickname target]
  (let [ok? (and (< 0 (count nickname)) (< (count nickname) 128))]
    (v/->result ok? (when (not ok?) "Nickname should have 1 - 128 characters.") target)))

(defn valid-password?
  [password target]
  (let [pwd-length-ok? (and (< 7 (count password)) (< (count password) 128))
        pwd-str-type? (v/validate v/password password target)
        ok? (and pwd-length-ok? (:ok? pwd-str-type?))]
    (v/->result ok? (when (not ok?) "Password should have 8 - 128 characters.") target)))

(defn valid-email?
  [email target]
  (v/validate v/email-format email target "Invalid Email Address."))

(defn valid-user-status?
  [user-status-id target]
  (let [ok? (not (empty? (r/find-user-status-by-id user-status-id)))]
    (v/->result ok? (when (not ok?) "Invalid user status.") target)))

(defn valid-user-id?
  [user-id target]
  (let [ok? (not (empty? (r/find-user user-id)))]
    (v/->result ok? (when (not ok?) "Invalid user id.") target)))

(defn is-active-user?
  [user-id target]
  (let [ok? (not (empty? (r/find-active-user user-id)))]
    (v/->result ok? (when (not ok?) "Invalid user id.") target)))

(defn validate-update
  [{:keys [user-id username nickname password email image-url user-status-id auth]}]
  (v/aggregate
   (valid-user-id? user-id :user-id)
   (valid-username? username :username)
   (valid-nickname? nickname :nickname)
   (valid-password? password :password)
   (valid-email? email :email)
   ;; todo implement validation for image-url
   (valid-user-status? user-status-id :user-status-id)
   (va/valid-auth? auth :auth)))
