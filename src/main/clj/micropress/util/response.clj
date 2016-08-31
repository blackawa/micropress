(ns micropress.util.response
  (:require [ring.util.response :as res]))

(defn created [url body]
  (res/created url body))

(defn bad-request [body]
  {:status  400
   :headers {}
   :body    body})

(defn res-api-ok [body]
  (let [body (if (string? body) body (pr-str body))]
    (res/response body)))

(defn html [res]
  (res/content-type res "text/html; charset=utf-8"))

(defn edn [res]
  (res/content-type res "application/edn; charset=utf-8"))
