(ns micropress.handler.draft
  (:require [compojure.core :refer [defroutes context GET POST PUT DELETE]]
            [micropress.service.draft :as d]
            [micropress.util.context :as context]
            [micropress.util.response :as res]
            [micropress.validator.draft :as vd]))

(defn- view-drafts
  [req]
  ;; 自分の「下書き」だけを取得する
  (let [user-id (context/get-user-id req)]
    (res/ok (d/find-all user-id))))

(defn- view-draft
  [req]
  ;; 自分の「下書き」なら取得する
  (let [article-id (get-in req [:params :article-id])
        user-id (context/get-user-id req)
        {:keys [ok? messages]} (vd/validate-view article-id user-id)]
    (if ok?
      (do (res/ok (d/find-by-id article-id user-id)))
      (res/bad-request messages))))

(defn- save-draft
  [req]
  (let [user-id (context/get-user-id req)
        params (-> (:params req)
                   (select-keys [:title :body :thumbnail-url :body-type :tags])
                   (assoc :user-id user-id))
        {:keys [ok? messages]} (vd/validate-save params)]
    (if ok?
      (do (d/save-draft params)
          (res/created "" {}))
      (res/bad-request messages))))

(defn- submit-draft
  [req]
  (let [user-id (context/get-user-id req)
        article-id (get-in req [:params :article-id])
        {:keys [ok? messages]} (vd/validate-submit article-id user-id)]
    (if ok?
      (do (d/submit-draft article-id)
          (res/ok))
      (res/bad-request messages))))

(defn- update-draft
  [req]
  (let [user-id (context/get-user-id req)
        params (-> (:params req)
                   (select-keys [:article-id :title :body :thumbnail-url :body-type :tags :submit?])
                   (assoc :user-id user-id))
        {:keys [ok? messages]} (vd/validate-update params)]
    (if ok?
      (do (d/update-draft params)
          (res/ok))
      (res/bad-request messages))))

(defn- delete-draft
  [req]
  (let [user-id (context/get-user-id req)
        article-id (get-in req [:params :article-id])
        {:keys [ok? messages]} (vd/validate-delete article-id user-id)]
    (if ok?
      (do (d/delete-draft article-id)
          (res/ok))
      (res/bad-request messages))))

(defroutes routes
  (context "/draft" _
           (GET "/" _ view-drafts)
           (POST "/" _ save-draft)
           (GET "/:article-id" _ view-draft)
           (POST "/:article-id" _ submit-draft)
           (PUT "/:article-id" _ update-draft)
           (DELETE "/:article-id" _ delete-draft)))
