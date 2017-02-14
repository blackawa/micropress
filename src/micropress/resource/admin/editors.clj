(ns micropress.resource.admin.editors
  (:require [liberator.core :refer [resource]]
            [micropress.repository.editor :as editor]
            [micropress.resource.base :refer [authenticated]]))

(defn- handle-ok [ctx db]
  (let [editor-id (:micropress.resource.base/editor-id ctx)]
    (->> (editor/find-all {} {:connection db})
         (map #(dissoc % :password)))))

(defn editors [db]
  (resource
   (authenticated db)
   :allowed-methods [:get]
   :available-media-types ["application/edn"]
   :handle-ok #(handle-ok % db)))
