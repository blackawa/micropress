(ns micropress.handler.auth
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.util.response :as res]
            [micropress.service.auth :as auth]
            [clojure.edn :as edn]))

(defn create-auth-token
  [req]
  (let [params (:params req)
        email (:email params)
        pwd (:pwd params)]
    (-> (auth/authenticate email pwd)
      pr-str
      res/response
      res/edn)))

(defroutes routes
  (context "/create-auth-token" _
           (POST "/" _ create-auth-token)))
