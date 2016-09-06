(ns micropress.core
  (:require [compojure.core :refer [defroutes routes context GET wrap-routes]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [ring.adapter.jetty :as server]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [micropress.handler.auth :as auth]
            [micropress.handler.invite :as invite]
            [micropress.middleware :refer [wrap-edn-response wrap-authentication]]))

(defonce server (atom nil))

(def ^:private public-api-routes
  (routes auth/routes))
(def ^:private secure-api-routes
  (routes invite/routes))

(def app
  ;; routes which do not require authorization token.
  (routes (-> public-api-routes
              ;; middleware for request
              (wrap-routes wrap-edn-params)
              ;; middleware for response
              (wrap-routes wrap-edn-response))
          (-> secure-api-routes
              ;; middleware for request
              (wrap-routes wrap-authentication) ;; <- stop process if invalid
              (wrap-routes wrap-edn-params)
              ;; middleware for response
              (wrap-routes wrap-edn-response))
          (route/not-found "404 Not found.")))

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
