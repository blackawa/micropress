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
        pwd (:pwd params)
        [ok? res] (auth/find-user email pwd)]
    (if ok?
      (res/created "Sorry, location url is under construction." (assoc res :token (auth/create-token res)))
      (res/bad-request res))))

(defroutes routes
  (context "/create-auth-token" _
           (POST "/" _ create-auth-token)))
