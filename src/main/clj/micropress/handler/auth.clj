(ns micropress.handler.auth
  (:require [compojure.core :refer [defroutes context POST]]
            [micropress.util.response :as res]
            [micropress.service.auth :as auth]
            [micropress.repository :as repo]))

(defn create-auth-token
  [req]
  (let [params (:params req)
        email (:email params)
        pwd (:pwd params)
        [ok? res] (auth/find-user email pwd)]
    (if ok?
      (let [token (auth/create-token res)]
        (repo/insert-session (:id res) token)
        (res/created "" (assoc res :token token)))
      (res/bad-request res))))

(defroutes routes
  (context "/api/create-auth-token" _
           (POST "/" _ create-auth-token)))
