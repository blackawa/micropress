(ns micropress.resource.admin.authentication
  (:require [buddy.hashers :as h]
            [clojure.tools.logging :as log]
            [clj-time.core :as time]
            [clj-time.jdbc]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.auth-token :as token]
            [micropress.repository.editor :as editor]))

(defn- malformed? [ctx db]
  (let [body (-> ctx (get-in [:request :body]) slurp)]
    (try
      (if-let [data (read-string body)]
        [false {::data data}]
        [true {::error "invalid format"}])
      (catch RuntimeException e
        (log/warnf "request with invalid format: %s" body)
        [true {::error "invalid format"}]))))

(defn- handle-malformed [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- processable? [ctx db]
  (let [{:keys [username password]} (::data ctx)
        editor (first (editor/find-active-by-username {:username username} {:connection db}))]
    (if (nil? editor)
      [false {::error "invalid username or password"}]
      (if (h/check password (:password editor))
        [true {::editor editor}]
        (do
          [false {::error "invalid username or password"}])))))

(defn- handle-unprocessable-entity [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- post! [ctx db]
  (let [editor-id (-> ctx ::editor :id)
        token (str (java.util.UUID/randomUUID))]
    ;; TODO: use JWT
    (token/create-auth-token! {:editor_id editor-id :token token :expire (time/plus (time/now) (time/days 30))}
                              {:connection db})
    {::token token}))

(defn- handle-created [{token ::token}]
  (ring-response {:body (str {:token token})}))

(defresource authentication [db]
  :allowed-methods [:post]
  :available-media-types ["application/edn"]
  :malformed? (fn [ctx] (malformed? ctx db))
  :handle-malformed handle-malformed
  :processable? (fn [ctx] (processable? ctx db))
  :handle-unprocessable-entity handle-unprocessable-entity
  :post! (fn [ctx] (post! ctx db))
  :handle-created handle-created)
