(ns micropress.resource.admin.articles
  (:require [clj-time.core :as time]
            [clj-time.format :as format]
            [clj-time.jdbc]
            [clojure.tools.logging :as log]
            [liberator.core :refer [resource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.article :as article]
            [micropress.repository.auth-token :as token]
            [micropress.repository.editor :as editor]
            [micropress.resource.base :refer [authenticated]]))

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
  (let [data (:micropress.resource.base/data ctx)
        article-status-id (article-status-id (:save-type data))
        editor-id (:micropress.resource.base/editor-id ctx)
        new-article (-> data
                       (assoc :article_status_id article-status-id)
                       (dissoc :save-type)
                       (assoc :editor_id editor-id)
                       (assoc :published_date (when (= :published (:save-type data)) (time/now)))
                       (article/create-article<! {:connection db}))]
    {::id (:id new-article)}))

(defn- location [ctx]
  (if-let [id (::id ctx)]
    (format "/admin/articles/%s" id)))

(defn articles [db]
  (resource
   (authenticated db)
   :allowed-methods [:get :post]
   :available-media-types ["application/edn"]
   :handle-ok (fn [ctx] (handle-ok ctx db))
   :post! (fn [ctx] (post! ctx db))
   :location location))
