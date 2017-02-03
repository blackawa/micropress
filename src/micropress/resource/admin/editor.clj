(ns micropress.resource.admin.editor
  (:require [buddy.hashers :as h]
            [liberator.core :refer [resource]]
            [micropress.repository.editor :as editor]
            [micropress.resource.base :refer [authenticated]]))

(defn- conflict? [ctx]
  (let [id-from-params (-> ctx :micropress.resource.base/params :id)
        id-from-token (:micropress.resource.base/editor-id ctx)]
    (if (= (str id-from-params) (str id-from-token))
      false
      [true {::error "invalid request"}])))

(defn- handle-conflict [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- put! [ctx db]
  (let [data (:micropress.resource.base/data ctx)
        editor-id (:micropress.resource.base/editor-id ctx)]
    (-> data
        (assoc :id editor-id)
        (update :password h/encrypt)
        (editor/update-editor! {:connection db}))))

(defn editor [db]
  (resource
   (authenticated db)
   :allowed-methods [:put]
   :available-media-types ["application/edn"]
   :conflict? conflict?
   :handle-conflict handle-conflict
   :put! #(put! % db)))
