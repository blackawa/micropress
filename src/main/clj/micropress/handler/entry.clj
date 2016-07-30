(ns micropress.handler.entry
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [micropress.util.response :as res]))

(def entries [{:id 1 :content "hello----" :author-id 1}
              {:id 2 :content "good evening." :author-id 2}])

(defn entries-view [req]
  [{:id 1 :content "hello----" :author-id 1}
   {:id 2 :content "good evening." :author-id 2}])

(defn entries [req]
  (-> (entries-view req)
      pr-str
      res/response
      res/edn))

(defroutes routes
  (context "/entry" _
           (GET "/" _ entries)))
