(ns micropress.handler.article
  (:require [clj-time.format :as time]
            [compojure.core :refer [defroutes context POST]]
            [micropress.service.article :as article]
            [micropress.util.context :as context]
            [micropress.util.response :as res]
            [micropress.validator.article :as va]))

(defn- publish
  [req]
  (let [{:keys [article-id publish-at]} (:params req)
        {:keys [ok? messages]} (va/validate-publish article-id publish-at)]
    (if ok?
      (do (article/publish article-id publish-at)
          (res/ok))
      (res/bad-request messages))))

(defroutes routes
  (context "/article" _
           (POST "/:article-id" _ publish)))
