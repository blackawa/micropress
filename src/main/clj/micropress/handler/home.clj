(ns micropress.handler.home
  (:require [compojure.core :refer [defroutes GET]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [micropress.util.response :as res]))

(defn home-view [req]
  (hc/html [:h1 "this is home!"]))

(defn home [req]
  (-> (home-view req)
      res/response
      res/html))

(defroutes routes
  (GET "/" _ home))
