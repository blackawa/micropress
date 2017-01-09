(ns micropress.resource.customer.articles
  (:require [clj-time.jdbc]
            [clj-time.format :as format]
            [environ.core :refer [env]]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.article :as article]))

(defn- handle-ok [ctx db]
  (ring-response
   (->> (article/find-all-published {} {:connection db})
        (map (fn [a] (update a :published_date #(format/unparse (format/formatter "yyyy/MM/dd") %)))))
   {:headers {"Access-Control-Allow-Origin" (:access-control-allow-origin env)}}))

(defn- handle-options [ctx]
  (ring-response "" {:headers {"Access-Control-Allow-Methods" "GET"
                               "Access-Control-Allow-Origin" (:access-control-allow-origin env)
                               "Access-Control-Allow-Headers" "Content-Type"}}))

(defresource articles [db]
  :allowed-methods [:get :options]
  :available-media-types ["application/edn"]
  :handle-options handle-options
  :handle-ok (fn [ctx] (handle-ok ctx db)))
