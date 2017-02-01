(ns micropress.resource.base
  (:require [clj-time.core :as time]
            [micropress.repository.auth-token :as token]))

;; TODO: 各フェーズの関数をバラして、 [false {::hoge fuga}]を返すようにする.
;; それを必要な分だけ繰り返せばmalformed?が分かるようにしてDRYにしたい.
;; malformed?などを共通関数に切り出すと、 ::data などが次の関数に渡せなくなる.
(defn- malformed? "認証トークンと(post / putなら)リクエストボディのパースを行う." [ctx]
  (let [auth-token (some-> ctx
                           (get-in [:request :headers "authorization"])
                           (clojure.string/split #"\s")
                           second)
        params (-> ctx (get-in [:request :params]))
        body (when (#{:post :put} (get-in ctx [:request :request-method])) (-> ctx (get-in [:request :body]) slurp))]
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

(defn authenticated [db]
  {:malformed? malformed?
   :handle-malformed handle-malformed
   :authorized? #(authorized? % db)})
