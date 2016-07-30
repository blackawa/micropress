(ns micropress.core
  (:require [compojure.core :refer [defroutes context GET]]
            [compojure.route :as route]
            [ring.adapter.jetty :as server]))

(defonce server (atom nil))

(defn ok [body]
  {:status 200
   :body body})

(defn html [res]
  (assoc res :headaers {"Content-type" "text/html; charset=utf-8"}))

(defn home-view [req]
  "<h1> home </h1>")

(defn entry [req]
  "<h1> entry </h1>")

(defn home [req]
  (-> (home-view req)
      ok
      html))

(defroutes handler
  (GET "/" req home)
  (GET "/entry/:id" req entry)
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
