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
   :post! #(post! % db)))
