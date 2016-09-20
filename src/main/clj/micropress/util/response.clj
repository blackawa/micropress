(ns micropress.util.response
  (:require [ring.util.response :as res]))

(defn created [url body]
  (res/created url body))

(defn bad-request [body]
  {:status  400
   :headers {}
   :body    body})

(defn unauthorized
  ([headers]
   (unauthorized headers ""))
  ([headers body]
   {:status 401
    :headers headers
    :body body}))

(defn forbidden
  ([headers]
   (forbidden headers ""))
  ([headers body]
   {:status 403
    :headers headers
    :body body}))

(defn accepted
  ([]
   (accepted ""))
  ([body]
  {:status 202
   :headers {}
   :body body}))

(defn ok
  ([]
   (ok {}))
  ([body]
   (res/response body)))

(defn not-found
  ([]
   (not-found {}))
  ([body]
   (res/not-found body)))
