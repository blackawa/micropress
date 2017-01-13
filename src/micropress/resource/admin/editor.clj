(ns micropress.resource.admin.editor
  (:require [buddy.hashers :as h]
            [liberator.core :refer [resource]]
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

(defn- put! [ctx db]
  (let [data (:micropress.resource.base/data ctx)
        editor-id (:micropress.resource.base/editor-id ctx)]
    (-> data
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
