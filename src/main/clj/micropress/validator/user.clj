(ns micropress.validator.user
  (:require [clj-time.core :as t]
            [micropress.repository :as r]
            [micropress.service.invite :as invite]
            [micropress.util.validator :as v]
            [micropress.validator.auth :as va]
            [schema.core :as s]))

(defn valid-username?
  [username]
  (let [[un-str-type? _] (v/validate v/username username)
        un-length-ok? (< (count username) 128)
        ok? (and un-str-type? un-length-ok?)]
    [ok? {:msg (when (not ok?) "Username should be less than 128, and can contain a-z, A-Z, 0-9, '-' and '_'.") :target username}]))

(defn valid-nickname?
  [nickname]
  (let [ok? (and (< 0 (count nickname)) (< (count nickname) 128))]
    [ok? {:msg (when (not ok?) "Nickname should have 1 - 128 characters.") :target nickname}]))

(defn valid-password?
  [password]
  (let [pwd-length-ok? (and (< 7 (count password)) (< (count password) 128))
        [pwd-str-type? _] (v/validate v/password password)
        ok? (and pwd-length-ok? pwd-str-type?)]
    [ok? {:msg (when (not ok?) "Password should have 8 - 128 characters.") :target password}]))

(defn valid-email?
  [email]
  (v/validate v/email-format email "Invalid Email Address."))

(defn valid-user-status?
  [user-status-id]
  ;; todo implemnt me
  (if-let [ok? (not (empty? (r/find-user-status-by-id user-status-id)))]
    [true {:msg nil :target user-status-id}]
    [false {:msg "Invalid user status." :target user-status-id}]))

(defn valid-user-id?
  [user-id]
  (if-let [ok? (not (empty? (r/find-user user-id)))]
    [true {:msg nil :target user-id}]
    [false {:msg "Invalid user id." :target user-id}]))

(defn is-active-user?
  [user-id]
  (if-let [ok? (not (empty? (r/find-active-user user-id)))]
    [true {:msg nil :target user-id}]
    [false {:msg "Invalid user id." :target user-id}]))

(defn validate-update
  [{:keys [user-id username nickname password email image-url user-status-id auth]}]
  (v/aggregate
   (valid-user-id? user-id)
   (valid-username? username)
   (valid-nickname? nickname)
   (valid-password? password)
   (valid-email? email)
   ;; todo implement validation for image-url
   (valid-user-status? user-status-id)
   (va/valid-auth? auth)))
