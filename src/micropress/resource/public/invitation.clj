(ns micropress.resource.public.invitation
  (:require [buddy.hashers :as h]
            [liberator.core :refer [resource]]
            [micropress.repository.editor :as editor]
            [micropress.repository.invitation :as invitation]
            [micropress.resource.base :refer [public]]))

(defn- exists? [ctx db]
  (let [params (:micropress.resource.base/params ctx)
        invitation (first (invitation/find-by-token {:token (:token params)} {:connection db}))]
    [(not (nil? invitation)) {::invitation invitation}]))

(defn- processable? [ctx db]
  (if (#{:post} (get-in ctx [:request :request-method]))
    (let [data (:micropress.resource.base/data ctx)
          new-username? (empty? (editor/find-active-by-username {:username (:username data)} {:connection db}))]
      [new-username? (when (not new-username?) {::error "username conflicted"})])
    true))

(defn- handle-unprocessable-entity [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- post! [ctx db]
  (let [data (:micropress.resource.base/data ctx)
        invitation (::invitation ctx)]
    (invitation/accept! {:id (:id invitation)} {:connection db})
    (-> data
        (update :password h/encrypt)
        (assoc :editor_status_id 1)
        (editor/create-editor! {:connection db}))))

(defn invitation [db]
  (resource
   (public)
   :allowed-methods [:get :post]
   :available-media-types ["application/edn"]
   :exists? #(exists? % db)
   :can-post-to-missing? (fn [ctx] false)
   :processable? #(processable? % db)
   :handle-unprocessable-entity handle-unprocessable-entity
   :post! #(post! % db)))
