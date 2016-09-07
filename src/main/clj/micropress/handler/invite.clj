(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]))

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req]
    (println email "," auth)))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
