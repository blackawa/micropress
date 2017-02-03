(ns micropress.resource.admin.editors
  (:require [buddy.hashers :as h]
            [liberator.core :refer [resource]]
            [micropress.repository.editor :as editor]
            [micropress.repository.invitation :as invitation]
            [micropress.resource.base :refer [authenticated]]))

(defn- handle-ok [ctx db]
  (let [editor-id (:micropress.resource.base/editor-id ctx)]
    (->> (editor/find-all {} {:connection db})
         (map #(dissoc % :password)))))

(defn- post! [ctx db]
  (let [token (str (java.util.UUID/randomUUID))]
    (invitation/create-invitation<! {:token token :invitation_status_id 1}
                                    {:connection db})
    {::token token}))

(defn- location [ctx]
  (if-let [token (::token ctx)]
    (format "/invitation/%s" token)))

(defn editors [db]
  (resource
   (authenticated db)
   :allowed-methods [:get :post]
   :available-media-types ["application/edn"]
   :handle-ok #(handle-ok % db)
   :post! #(post! % db)
   :location location))
