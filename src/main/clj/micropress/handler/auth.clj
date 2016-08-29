(ns micropress.handler.auth
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.util.response :as res]
            [micropress.service.auth :as auth]
            [clojure.edn :as edn]))

(defn authenticate
  [req]
  (println req)
  (let [params (edn/read-string (:body req))
        email (:email params)
        pwd (:pwd params)]
    (println params)
    (-> (auth/authenticate email pwd)
      pr-str
      res/response
      res/edn)))

(defroutes routes
  (context "/authenticate" _
           (POST "/" _ authenticate)))
