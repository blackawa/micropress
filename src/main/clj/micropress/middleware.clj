(ns micropress.middleware
  (:require [clojure.string :as str]
            [clojure.tools.logging :as log]
            [ring.util.response :as res]
            [micropress.const.authorization :as const]
            [micropress.service.auth :as auth]
            [micropress.service.authorization :as authorization]
            [micropress.util.response :refer [unauthorized forbidden internal-server-error]])
  (:import [org.slf4j MDC]))

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
    (let [token (get-authorization-token req)]
      (if (auth/validate-token token)
        (handler req)
        (unauthorized {"Authorization" (format "Bearer error=invalid token.[%s]" token)})))))

(defn wrap-context
  "ユーザーがログインしていればその情報をリクエストに付加して
   つぎのhandlerに渡す"
  [handler]
  (fn [req]
    (if-let [user-id (:users_id (authorization/find-by-token (get-authorization-token req)))]
      (handler (assoc req :context {:user-id user-id}))
      (handler req))))

(defn- valid-authorization?
  "ユーザーとその権限を引いて、ユーザーがそのパスとHTTPメソッドを実行可能かチェックする.
   トークン自体の有効性はこの関数の責務外."
  [token uri request-method]
  (let [authorizations (set (map #(:id %) (authorization/find-by-token token)))]
    (->> const/uri-method-defs
         (filter #(if (string? (:uri %))
                    (= uri (:uri %))
                    (not (nil? (re-find (:uri %) uri)))))
         (filter #(= request-method (:request-method %)))
         ;; filter by user's authorities
         (filter #(some authorizations (:auth %)))
         empty?
         not)))

(defn wrap-authorization
  "Check authorization status.
   If the request user does not have privilege enough to execute the process,
   I return 403 error. Otherwise, I execute preceeding processes.
   I will not check if the given token is valid or not."
  [handler]
  (fn [req]
    (if (valid-authorization? (get-authorization-token req) (:uri req) (:request-method req))
      (handler req)
      (forbidden {}))))

(defn wrap-log-mdc
  [handler]
  (fn [req]
    (let [request-id (str "req-" (.getId (Thread/currentThread)) "-" (System/currentTimeMillis))]
      (MDC/put "request-id" request-id)
      (log/info (:uri req))
      (handler req))))

(defn wrap-log-response
  [handler]
  (fn [req]
    (let [res (handler req)]
      (log/info (str "response status " (:status res)))
      res)))

(defn wrap-exception
  [handler]
  (fn [req]
    (try (handler req)
         (catch Exception e (do (log/error (str/join "\n        at " (map str (.getStackTrace e))))
                                (internal-server-error "Internal Server Error."))))))
