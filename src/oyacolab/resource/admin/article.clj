(ns oyacolab.resource.admin.article
  (:require [clj-time.core :as time]
            [clj-time.jdbc]
            [clj-time.format :as format]
            [clojure.tools.logging :as log]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [oyacolab.repository.article :as article]
            [oyacolab.repository.auth-token :as token]
            [oyacolab.repository.editor :as editor]))

;; TODO: 各フェーズの関数をバラして、 [false {::hoge fuga}]を返すようにする.
;; それを必要な分だけ繰り返せばmalformed?が分かるようにしてDRYにしたい.
;; malformed?などを共通関数に切り出すと、 ::data などが次の関数に渡せなくなる.
(defn- malformed? "認証トークンと(post / putなら)リクエストボディのパースを行う." [ctx]
  (let [auth-token (second (clojure.string/split (-> ctx (get-in [:request :headers]) (get "authorization")) #"\s"))
        params (-> ctx (get-in [:request :params]))
        body (when (#{:put} (get-in ctx [:request :request-method])) (-> ctx (get-in [:request :body]) slurp))]
    (if auth-token
      (if body
        (try
          [false {::auth-token auth-token ::data (read-string body) ::params params}]
          (catch RuntimeException e
            [true {::error "invalid body"}]))
        [false {::auth-token auth-token ::params params}])
      [true {::error "invalid auth token"}])))

(defn- handle-malformed [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- authorized? [ctx db]
  (let [auth-token (first (token/find-by-token {:token (::auth-token ctx)} {:connection db}))]
    (when (time/before? (time/now) (:expire auth-token))
      [true {::editor-id (:editor_id auth-token)}])))

(defn- conflict? [ctx]
  (let [id (-> ctx ::params :id)
        data (::data ctx)]
    (if (= (str id) (str (:id data)))
      false
      [true {::error "invalid request"}])))

(defn- handle-conflict [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- save-type [article-status-id]
  (condp = article-status-id
    1 :draft
    2 :published
    3 :withdrawn))

(defn- handle-ok [ctx db]
  (let [id (-> ctx ::params :id)
        article (first (article/find-by-id {:id (read-string id)} {:connection db}))]
    (-> article
        (assoc :save-type (save-type (:article_status_id article)))
        (dissoc :article_status_id)
        (update :published_date #(format/unparse (format/formatter "yyyy/MM/dd") %)))))

(defn- article-status-id [save-type]
  (condp = save-type
    :draft 1
    :published 2
    :withdrawn 3))

(defn- put! [ctx db]
  (let [data (::data ctx)
        article-status-id (article-status-id (:save-type data))
        editor-id (::editor-id ctx)]
    (-> ctx
        ::data
        (assoc :article_status_id article-status-id)
        (dissoc :save-type)
        (assoc :editor_id editor-id)
        (assoc :published_date (when (= :published (:save-type data)) (time/now)))
        (article/update-article! {:connection db}))))

(defresource article [db]
  :allowed-methods [:get :put]
  :available-media-types ["application/edn"]
  :malformed? malformed?
  :handle-malformed handle-malformed
  :authorized? (fn [ctx] (authorized? ctx db))
  :conflict? conflict?
  :handle-conflict handle-conflict
  :handle-ok (fn [ctx] (handle-ok ctx db))
  :put! (fn [ctx] (put! ctx db)))
