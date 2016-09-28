(ns micropress.core
  (:require [compojure.core :refer [defroutes routes context GET wrap-routes]]
            [compojure.route :as route]
            [hiccup.core :as hc]
            [ring.adapter.jetty :as server]
            [ring.middleware.edn :refer [wrap-edn-params]]
            [micropress.handler.auth :as auth]
            [micropress.handler.invite :as invite]
            [micropress.handler.join :as join]
            [micropress.handler.user :as user]
            [micropress.middleware :refer [wrap-edn-response wrap-authentication wrap-authorization
                                           wrap-log-mdc wrap-log-response wrap-exception]]))

(defonce server (atom nil))

(def ^:private public-api-routes
  (routes
   (context "/api" _
            auth/routes
            join/routes)))
(def ^:private secure-api-routes
  (routes
   (context "/api" _
            invite/routes
            user/routes)))

(def app
  ;; routes which do not require authorization token.
  (routes (-> secure-api-routes
              ;; middleware for request
              (wrap-routes wrap-log-mdc)
              (wrap-routes wrap-authentication)
              (wrap-routes wrap-authorization)
              (wrap-routes wrap-edn-params)
              ;; middleware for response
              (wrap-routes wrap-edn-response)
              (wrap-routes wrap-exception)
              (wrap-routes wrap-log-response))
          (-> public-api-routes
              ;; middleware for request
              (wrap-routes wrap-log-mdc)
              (wrap-routes wrap-edn-params)
              ;; middleware for response
              (wrap-routes wrap-edn-response)
              (wrap-routes wrap-exception)
              (wrap-routes wrap-log-response))
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
