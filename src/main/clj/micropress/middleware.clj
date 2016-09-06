(ns micropress.middleware
  (:require [clojure.string :as str]
            [ring.util.response :as res]
            [micropress.service.auth :as auth]
            [micropress.util.response :refer [unauthorized]]))

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

(defn- get-authorization-token
  [req]
  (if-let [v (-> req :headers (get "authorization"))]
    (let [[type token] (str/split v #"\s+")]
      (when (= "Bearer" type)
        token))))

(defn wrap-authentication
  "Check authentication status.
   If the request header contains 'Authorization: Bearer xxx', I validate it.
   I return 401 error if it is invalid token, and I execute handler if it is valid."
  [handler]
  (fn [req]
    (if-let [token (get-authorization-token req)]
      (if (auth/validate-token token)
        (handler req)
        (unauthorized {"uthorization" (format "Bearer error=invalid token.[%s]" token)})))))
