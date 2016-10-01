(ns micropress.validator.join
  (:require [micropress.util.validator :as v]
            [micropress.validator.invite :as vi]
            [micropress.validator.user :as vu]))

(defn validate-acception
  [{:keys [token username nickname password]}]
  (v/aggregate
   (vi/valid-invitee-token? token :token)
   (vu/valid-username? username :username)
   (vu/valid-nickname? nickname :nickname)
   (vu/valid-password? password :password)))
