(ns micropress.validator.join
  (:require [micropress.repository :as r]
            [micropress.service.invite :as invite]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(defn- valid-invitee-token?
  [token]
  (let [ok? (not (nil? (r/find-invitee-by-token token)))]
    (if ok?
      [true {:msg nil :target token}]
      [false {:msg "Invalid token." :target token}])))

(defn- valid-username?
  [username]
  (let [[un-str-type? _] (v/validate v/username username)
        un-length-ok? (< (count username) 128)
        ok? (and un-str-type? un-length-ok?)]
    [ok? {:msg (when (not ok?) "Username should be less than 128, and can contain a-z, A-Z, 0-9, '-' and '_'.") :target username}]))

(defn- valid-nickname?
  [nickname]
  (let [ok? (< (count nickname) 128)]
    [ok? {:msg (when (not ok?) "Nickname should have 1 - 128 characters.") :target nickname}]))

(defn- valid-password?
  [password]
  (let [pwd-length-ok? (and (< 7 (count password)) (< (count password) 128))
        [pwd-str-type? _] (v/validate v/password password)
        ok? (and pwd-length-ok? pwd-str-type?)]
    [ok? {:msg (when (not ok?) "Password should have 8 - 128 characters.") :target password}]))

(defn validate-acception
  [token username nickname password]
  (v/aggregate
   (valid-invitee-token? token)
   (valid-username? username)
   (valid-nickname? nickname)
   (valid-password? password)))
