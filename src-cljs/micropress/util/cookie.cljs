(ns micropress.util.cookie
  (:import [goog.net Cookies]))

(defn get-cookie [k]
  (.get (Cookies. js/document) (name k)))

(defn set-cookie! [k v]
  (.set goog.net.cookies (name k) v (* 60 60 24 10) "/"))
