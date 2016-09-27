(ns micropress.handler.user
  (:require [compojure.core :refer [defroutes context GET PUT]]
            [micropress.service.user :as u]
            [micropress.util.response :as res]
            [micropress.validator.user :as vu]))

(defn- view-users
  [req]
  (res/ok (u/view-users)))

(defn- view-user
  [req]
  (let [user-id (get-in req [:params :user-id])
        user (u/view-user user-id)]
    (if (not (nil? user))
      (res/ok user)
      (res/not-found "User not found."))))

(defn- update-user
  [req]
  (let [params (select-keys (:params req) [:user-id :username :nickname :password :email
                                           :image-url :user-status-id :auth])
        {:keys [ok? messages]} (vu/validate-update params)]
    (if ok?
      (res/ok (u/update-user params))
      (res/bad-request messages))))

(defroutes routes
  (context "/user" _
           (GET "/" _ view-users)
           (GET "/:user-id" _ view-user)
           (PUT "/:user-id" _ update-user)))
