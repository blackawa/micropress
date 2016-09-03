(ns micropress.util.response
  (:require [ring.util.response :as res]))

(defn created [url body]
  (res/created url body))

(defn bad-request [body]
  {:status  400
   :headers {}
   :body    body})

(defn unauthorized
  ([body]
   (unauthorized {} body))
  ([headers body]
   {:status 401
    :headers headers
    :body body}))
