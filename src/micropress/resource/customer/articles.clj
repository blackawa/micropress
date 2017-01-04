(ns micropress.resource.customer.articles
  (:require [clj-time.jdbc]
            [clj-time.format :as format]
            [liberator.core :refer [defresource]]
            [micropress.repository.article :as article]))

(defn- handle-ok [ctx db]
  (->> (article/find-all-published {} {:connection db})
       (map (fn [a] (update a :published_date #(format/unparse (format/formatter "yyyy/MM/dd") %))))))

(defresource articles [db]
  :allowed-methods [:get]
  :available-media-types ["application/edn"]
  :handle-ok (fn [ctx] (handle-ok ctx db)))
