(ns micropress.handler.draft
  (:require [compojure.core :refer [defroutes context POST]]
            [micropress.service.draft :as d]
            [micropress.util.response :as res]
            [micropress.validator.draft :as vd]))

(defn- save-draft
  [req]
  (let [user-id (get-in req [:context :user-id])
        params (-> (:params req)
                   (select-keys [:title :content :thumbnail-url :body-type :tags])
                   (assoc :user-id user-id))
        [ok? msg] (vd/validate-save params)]
    (if ok?
      (d/save-draft params)
      (res/bad-request msg))))

(defroutes routes
  (context "/draft" _
           (POST "/" _ save-draft)))
