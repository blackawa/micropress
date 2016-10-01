(ns micropress.handler.join
  (:require [compojure.core :refer [defroutes context GET POST]]
            [micropress.service.invite :as i]
            [micropress.service.join :as j]
            [micropress.util.response :as res]
            [micropress.validator.join :as v]))

(defn- view-invitee
  [req]
  (let [token (-> req :params :token)
        invitee (i/view-invitee token)]
    (if (not (nil? invitee))
      (res/ok invitee)
      (res/not-found))))

(defn- accept-invitation
  [req]
  (let [params (:params req)
        {:keys [ok? messages]} (v/validate-acception params)]
    (if ok?
      (res/ok (j/accpet-invitation params))
      (res/bad-request messages))))

(defroutes routes
  (context "/invitation" _
           (GET "/:token" _ view-invitee)
           (POST "/:token" _ accept-invitation)))
