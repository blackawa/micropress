(ns micropress.core
  (:require [ring.adapter.jetty :as server]))

(defonce server (atom nil))

(defn handler [req]
  {:status 200
   :headers {"Content-Type" "text/plain; charset=utf-8"}
   :body "Hello, world"})

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
