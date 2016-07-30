(ns micropress.util.response
  (:require [ring.util.response :as res]))

(defn response [body]
  (res/response body))

(defn html [res]
  (res/content-type res "text/html; charset=utf-8"))
