(ns micropress.handler.user
  (:require [compojure.core :refer [defroutes context GET]]
            [micropress.service.user :as u]
            [micropress.util.response :as res]))

(defn- view-users
  [req]
  (res/ok (u/view-users)))

(defn- view-user
  [req]
  (let [user-id (get-in req [:params :user-id])]
    (res/ok (u/view-user user-id))))

(defroutes routes
  (context "/user" _
           (GET "/" _ view-users)
           (GET "/:user-id" _ view-user)))
