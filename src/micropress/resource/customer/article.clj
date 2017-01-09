(ns micropress.resource.customer.article
  (:require [clj-time.jdbc]
            [clj-time.format :as format]
            [liberator.core :refer [defresource]]
            [micropress.repository.article :as article]))

(defn- malformed? [ctx]
  (let [params (-> ctx (get-in [:request :params]))]
    [false {::params params}]))

(defn- handle-ok [ctx db]
  (let [params (::params ctx)]
    (-> (article/find-published-by-id {:id (read-string (:id params))} {:connection db})
        first
        (update :published_date #(format/unparse (format/formatter "yyyy/MM/dd") %)))))

(defresource article [db]
  :allowed-methods [:get]
  :available-media-types ["application/edn"]
  :malformed? malformed?
  :handle-ok (fn [ctx] (handle-ok ctx db)))
