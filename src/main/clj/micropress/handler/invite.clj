(ns micropress.handler.invite
  (:require [compojure.core :refer [defroutes context POST]]
            [compojure.route :as route]
            [micropress.service.invite :as invite]
            [micropress.util.response :as res]))

(defn- invite-user
  [req]
  (let [{{email :email auth :auth} :params} req
        ok? (and (invite/invite email auth)
                 (invite/send-invite-mail email))]
    (if ok?
      () ;; 処理は受け付けられました的なレスポンス返す
      () ;; 例外情報を返す？
      )))

(defroutes routes
  (context "/invite" _
           (POST "/" _ invite-user)))
