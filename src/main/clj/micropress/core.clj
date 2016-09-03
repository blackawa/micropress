(ns micropress.core
  (:require [compojure.core :refer [defroutes routes context GET]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [ring.adapter.jetty :as server]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [micropress.handler.auth :as auth]
            [micropress.middleware :refer [wrap-edn-response]]))

(defonce server (atom nil))

(defroutes app
  (-> (routes
       (routes auth/routes)
       (route/not-found "<h1>404 Not found</h1>"))
      ;; middleware for request
      wrap-edn-params
      ;; middleware for response
      wrap-edn-response))

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
