(ns micropress.handler.entry
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [micropress.util.response :as res]))

(defn entry-view [{:keys [params] :as req}]
  (hc/html [:h1 (str "entry " (:id params))]))

(defn entry [req]
  (-> (entry-view req)
      res/response
      res/html))

(defroutes routes
  (context "/entry"
           (GET "/:id" _ entry-view)))
