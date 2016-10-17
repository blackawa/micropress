(ns micropress.handler.auth
  (:require [compojure.core :refer [defroutes context POST]]
            [micropress.repository.session :as session]
            [micropress.service.auth :as auth]
            [micropress.util.response :as res]
            [micropress.util.validator :as v]
            [micropress.validator.auth :as va]))

(defn- renew-auth-token
  [req]
  (let [params (:params req)
        email (:email params)
        pwd (:pwd params)
        {:keys [ok? messages]} (va/validate-auth email pwd)]
    (if ok?
      (let [user (auth/find-user email pwd)]
        (if (not (nil? user))
          (let [token (auth/create-token user)]
            (session/delete-by-user-id (:id user))
            (session/insert-session (:id user) token)
            (res/created "" (assoc user :token token)))
          (res/bad-request (:messages (v/->result false "Invalid username or password" nil)))))
      (res/bad-request messages))))

(defroutes routes
  (context "/renew-auth-token" _
           (POST "/" _ renew-auth-token)))
