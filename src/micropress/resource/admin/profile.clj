(ns micropress.resource.admin.profile
  (:require [buddy.hashers :as h]
            [liberator.core :refer [resource]]
            [micropress.repository.editor :as editor]
            [micropress.resource.base :refer [authenticated]]))

(defn- handle-ok [ctx db]
  (let [editor-id (:micropress.resource.base/editor-id ctx)]
    (-> (editor/find-by-id {:id editor-id} {:connection db})
        first
        (dissoc :password))))

(defn profile [db]
  (resource
   (authenticated db)
   :allowed-methods [:get]
   :available-media-types ["application/edn"]
   :handle-ok #(handle-ok % db)))
