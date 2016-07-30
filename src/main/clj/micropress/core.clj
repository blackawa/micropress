(ns micropress.core
  (:require [compojure.core :refer [defroutes routes context GET]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [ring.adapter.jetty :as server]
            [micropress.handler.entry :as entry]))

(defonce server (atom nil))

(defroutes handler
  (routes entry/routes)
  (route/not-found "<h1>404 Not found</h1>"))

(defn start []
  (when-not @server
    (reset! server (server/run-jetty #'handler {:port 3000 :join? false}))))

(defn halt []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart []
  (when @server
    (halt)
    (start)))
