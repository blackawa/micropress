(ns micropress.handler.user
  (:require [compojure.core :refer [defroutes context GET]]
            [micropress.service.user :as u]
            [micropress.util.response :as res]
            [micropress.validator.user :as vu]))

(defn- view-users
  [req]
  (res/ok (u/view-users)))

(defn- view-user
  [req]
  (let [user-id (get-in req [:params :user-id])
        user (u/view-user user-id)]
    (if (not (nil? user))
      (res/ok user)
      (res/not-found "User not found."))))

(defn- update-user
  [req]
  (let [params (select-keys (:params req) [:username :nickname :password :email
                                           :image-url :user-status-id :authorities])
        [ok? msg] (vu/validate-update params)]
    (if ok?
      ;; ユーザー情報更新
      ;; ユーザー権限情報更新
      ;; メアドが変わってたら確認中テーブルに登録してメールを投げる.
      ""
      (res/bad-request msg))))

(defroutes routes
  (context "/user" _
           (GET "/" _ view-users)
           (GET "/:user-id" _ view-user)))
