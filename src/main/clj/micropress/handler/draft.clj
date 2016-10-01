(ns micropress.handler.draft
  (:require [compojure.core :refer [defroutes context POST]]
            [micropress.service.draft :as d]
            [micropress.util.response :as res]
            [micropress.validator.draft :as vd]))

(defn- save-draft
  [req]
  (let [user-id (get-in req [:context :user-id])
        params (-> (:params req)
                   (select-keys [:title :body :thumbnail-url :body-type :tags])
                   (assoc :user-id user-id))
        {:keys [ok? messages]} (vd/validate-save params)]
    (if ok?
      (do (d/save-draft params)
          (res/created "" {}))
      (res/bad-request messages))))

(defroutes routes
  (context "/draft" _
           (POST "/" _ save-draft)))
