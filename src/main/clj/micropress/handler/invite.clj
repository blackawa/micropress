(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]))

(defn invite-user
  [req]
  (println req))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
