(ns micropress.resource.admin.auth-token
  (:require [clj-time.core :as time]
            [clj-time.jdbc]
            [clojure.tools.logging :as log]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.auth-token :as token]
            [micropress.repository.editor :as editor]))

(defn- malformed? [ctx]
  (let [authorization (-> ctx (get-in [:request :headers]) (get "authorization"))]
    (if-let [auth-token (second (clojure.string/split authorization #"\s"))]
      [false {::data auth-token}]
      [true {::error "invalid auth token"}])))

(defn- handle-malformed [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- authorized? [ctx db]
  (let [auth-token (first (token/find-by-token {:token (::data ctx)} {:connection db}))]
    (time/before? (time/now) (:expire auth-token))))

(defresource auth-token [db]
  :allowed-methods [:get]
  :available-media-types ["application/edn"]
  :malformed? malformed?
  :authorized? (fn [ctx] (authorized? ctx db))
  :handle-malformed handle-malformed)
