(ns micropress.resource.customer.article
  (:require [clj-time.jdbc]
            [clj-time.format :as format]
            [environ.core :refer [env]]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.article :as article]))

(defn- malformed? [ctx]
  (let [params (-> ctx (get-in [:request :params]))]
    [false {::params params}]))

(defn- handle-options [ctx]
  (ring-response "" {:headers {"Access-Control-Allow-Methods" "GET"
                               "Access-Control-Allow-Origin" (:access-control-allow-origin env)
                               "Access-Control-Allow-Headers" "Content-Type"}}))

(defn- handle-ok [ctx db]
  (let [params (::params ctx)]
    (-> (article/find-published-by-id {:id (read-string (:id params))} {:connection db})
        first
        (update :published_date #(format/unparse (format/formatter "yyyy/MM/dd") %))
        (ring-response {:headers {"Access-Control-Allow-Origin" (:access-control-allow-origin env)}}))))

(defresource article [db]
  :allowed-methods [:get :options]
  :available-media-types ["application/edn"]
  :malformed? malformed?
  :handle-options handle-options
  :handle-ok (fn [ctx] (handle-ok ctx db)))
