(ns micropress.validator.invite
  (:require [micropress.repository :as r]
            [micropress.service.invite :as invite]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(def auth-format [s/Num])

(defn- isnt-he-user?
  "すでにユーザーでないかを調べる"
  [email]
  (if (empty? (r/find-user-by-email email))
    [true {:msg nil :target email}]
    [false {:msg (format "%s is already used." email) :target email}]))

(defn- valid-auth?
  "権限IDが正しいか調べる"
  [auth]
  (let [ok? (->> auth
                 (map #(not (nil? (r/find-auth-by-id %))))
                 (reduce (fn [b1 b2] (and b1 b2))))]
    (if ok?
      [true {:msg nil :target auth}]
      [false {:msg "Contains invalid auth."} :target auth])))

(defn validate
  [email auth]
  (v/aggregate
   (v/validate v/email-format email "Invalid Email Address.")
   (v/validate auth-format auth "Invalid Authorities.")
   (isnt-he-user? email)
   (valid-auth? auth)))
