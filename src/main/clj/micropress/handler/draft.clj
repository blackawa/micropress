(ns micropress.handler.draft
  (:require [compojure.core :refer [defroutes context POST PUT]]
            [micropress.service.draft :as d]
            [micropress.util.context :as context]
            [micropress.util.response :as res]
            [micropress.validator.draft :as vd]))

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
          (res/created "" {}))
      (res/bad-request messages))))

(defroutes routes
  (context "/draft" _
           (POST "/" _ save-draft)
           (POST "/:article-id" _ submit-draft)))
