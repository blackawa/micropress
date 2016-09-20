(ns micropress.validator.join
  (:require [clj-time.core :as t]
            [micropress.repository :as r]
            [micropress.service.invite :as invite]
            [micropress.util.validator :as v]
            [micropress.validator.invite :as vi]
            [micropress.validator.user :as vu]
            [schema.core :as s]))

(defn validate-acception
  [{:keys [token username nickname password]}]
  (v/aggregate
   (vi/valid-invitee-token? token)
   (vu/valid-username? username)
   (vu/valid-nickname? nickname)
   (vu/valid-password? password)))
