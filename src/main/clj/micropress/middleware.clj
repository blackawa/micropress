(ns micropress.middleware
  (:require [ring.util.response :as res]))

(defn- accept-edn? [req]
  (if-let [^String type (-> req :headers (get "accept"))]
    (not (empty? (re-find #"^application/(vnd.+)?edn" type)))))

(defn- ednify-body
  [res]
  (let [body (:body res)]
    (if (string? body)
      res
      (assoc res :body (pr-str body)))))

(defn wrap-edn-response
  "If the request requires edn response,
   convert response body to edn string."
  [handler]
  (fn [req]
    (let [res (handler req)]
      (if (accept-edn? req)
        (res/content-type (assoc res :body (pr-str (:body res))) "application/edn; charset=utf-8")
        res))))
