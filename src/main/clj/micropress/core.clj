(ns micropress.core
  (:require [compojure.core :refer [defroutes routes context GET]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [ring.adapter.jetty :as server]
            [ring.middleware.params :refer [wrap-params]]
            [micropress.handler.auth :as auth]))

(defonce server (atom nil))

(defroutes app
  (-> (routes
       (routes auth/routes)
       (route/not-found "<h1>404 Not found</h1>"))
      wrap-params))

(defn run []
  (when-not @server
    (reset! server (server/run-jetty #'app {:port 3000 :join? false}))))

(defn halt []
  (when @server
    (.stop @server)
    (reset! server nil)))

(defn restart []
  (when @server
    (halt)
    (run)))
