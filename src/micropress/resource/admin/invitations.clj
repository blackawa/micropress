(ns micropress.resource.admin.invitations
  (:require [liberator.core :refer [resource]]
            [micropress.repository.invitation :as invitation]
            [micropress.resource.base :refer [authenticated]]))

(defn- post! [ctx db]
  (let [token (str (java.util.UUID/randomUUID))]
    (invitation/create-invitation<! {:token token :invitation_status_id 1}
                                    {:connection db})
    {::token token}))

(defn- location [ctx]
  (if-let [token (::token ctx)]
    (format "/invitation/%s" token)))

(defn invitations [db]
  (resource
   (authenticated db)
   :allowed-methods [:post]
   :available-media-types ["application/edn"]
   :post! #(post! % db)
   :location location))
