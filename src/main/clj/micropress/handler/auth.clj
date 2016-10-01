(ns micropress.handler.auth
  (:require [compojure.core :refer [defroutes context POST]]
            [micropress.util.response :as res]
            [micropress.util.validator :as v]
            [micropress.service.auth :as auth]
            [micropress.repository.session :as session]))

(defn- renew-auth-token
  [req]
  (let [params (:params req)
        email (:email params)
        pwd (:pwd params)
        user (auth/find-user email pwd)]
    (if (not (nil? user))
      (let [token (auth/create-token user)]
        (session/delete-by-user-id (:id user))
        (session/insert-session (:id user) token)
        (res/created "" (assoc user :token token)))
      (res/bad-request (:messages (v/->result false "Invalid username or password" nil))))))

(defroutes routes
  (context "/renew-auth-token" _
           (POST "/" _ renew-auth-token)))
