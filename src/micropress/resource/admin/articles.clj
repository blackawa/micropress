(ns micropress.resource.admin.articles
  (:require [clj-time.core :as time]
            [clj-time.format :as format]
            [clj-time.jdbc]
            [clojure.tools.logging :as log]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.article :as article]
            [micropress.repository.auth-token :as token]
            [micropress.repository.editor :as editor]))

(defn- malformed? "認証トークンと(post / putなら)リクエストボディのパースを行う." [ctx]
  (let [authorization (-> ctx (get-in [:request :headers]) (get "authorization"))
        auth-token (second (clojure.string/split authorization #"\s"))
        body (when (#{:post} (get-in ctx [:request :request-method])) (-> ctx (get-in [:request :body]) slurp))]
    (if auth-token
      (if body
        (try
          [false {::auth-token auth-token ::data (read-string body)}]
          (catch RuntimeException e
            [true {::error "invalid body"}]))
        [false {::auth-token auth-token}])
      [true {::error "invalid auth token"}])))

(defn- handle-malformed [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- authorized? [ctx db]
  (let [auth-token (first (token/find-by-token {:token (::auth-token ctx)} {:connection db}))]
    (when (time/before? (time/now) (:expire auth-token))
      [true {::editor-id (:editor_id auth-token)}])))

(defn- handle-ok [ctx db]
  (map
   (fn [a] (update a :published_date #(format/unparse (format/formatter "yyyy/MM/dd") %)))
   (article/find-all {} {:connection db})))

(defn- article-status-id [save-type]
  (condp = save-type
    :draft 1
    :published 2
    :withdrawn 3))

(defn- post! [ctx db]
  (let [data (::data ctx)
        article-status-id (article-status-id (:save-type data))
        editor-id (::editor-id ctx)
        new-article (-> ctx
                       ::data
                       (assoc :article_status_id article-status-id)
                       (dissoc :save-type)
                       (assoc :editor_id editor-id)
                       (assoc :published_date (when (= :published (:save-type data)) (time/now)))
                       (article/create-article<! {:connection db}))]
    {::id (:id new-article)}))

(defn- location [ctx]
  (if-let [id (::id ctx)]
    (format "/admin/articles/%s" id)))

(defresource articles [db]
  :allowed-methods [:get :post]
  :available-media-types ["application/edn"]
  :malformed? malformed?
  :handle-malformed handle-malformed
  :authorized? (fn [ctx] (authorized? ctx db))
  :handle-ok (fn [ctx] (handle-ok ctx db))
  :post! (fn [ctx] (post! ctx db))
  :location location)
