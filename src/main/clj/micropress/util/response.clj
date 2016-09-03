(ns micropress.util.response
  (:require [ring.util.response :as res]))

(defn created [url body]
  (res/created url body))

(defn bad-request [body]
  {:status  400
   :headers {}
   :body    body})
