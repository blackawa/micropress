(ns micropress.validator.auth
  (:require [micropress.util.validator :as v]
            [micropress.validator.user :as vu]
            [schema.core :as s]))

(def auth-format [s/Num])

(defn pwd-input?
  [pwd target]
  (if (clojure.string/blank? pwd)
    (v/->result false "Password should be input" target)
    (v/->result true nil target)))

(defn validate-auth
  [email raw-pwd]
  (v/aggregate
   (vu/valid-email? email :email)
   (pwd-input? raw-pwd :password)))
