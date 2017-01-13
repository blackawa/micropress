(ns micropress.resource.admin.article
  (:require [clj-time.core :as time]
            [clj-time.jdbc]
            [clj-time.format :as format]
            [clojure.tools.logging :as log]
            [liberator.core :refer [resource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.article :as article]
            [micropress.repository.auth-token :as token]
            [micropress.repository.editor :as editor]
            [micropress.resource.base :refer [authenticated]]))

(defn- conflict? [ctx]
  (let [id (-> ctx :micropress.resource.base/params :id)
        data (:micropress.resource.base/data ctx)]
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
  (let [id (-> ctx :micropress.resource.base/params :id)
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
  (let [data (:micropress.resource.base/data ctx)
        article-status-id (article-status-id (:save-type data))
        editor-id (:micropress.resource.base/editor-id ctx)]
    (-> data
        (assoc :article_status_id article-status-id)
        (dissoc :save-type)
        (assoc :editor_id editor-id)
        (assoc :published_date (when (= :published (:save-type data)) (time/now)))
        (article/update-article! {:connection db}))))

(defn article [db]
  (resource
   (authenticated db)
   :allowed-methods [:get :put]
   :available-media-types ["application/edn"]
   :conflict? conflict?
   :handle-conflict handle-conflict
   :handle-ok (fn [ctx] (handle-ok ctx db))
   :put! (fn [ctx] (put! ctx db))))
