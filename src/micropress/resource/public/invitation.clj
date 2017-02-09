(ns micropress.resource.public.invitation
  (:require [liberator.core :refer [resource]]
            [micropress.repository.invitation :as invitation]
            [micropress.resource.base :refer [public]]))

(defn- exists? [ctx db]
  (not (empty? (invitation/find-by-token (first (:micropress.resource.base/params ctx))))))

(defn invitation [db]
  (resource
   (public)
   :allowed-methods [:get]
   :available-media-types ["application/edn"]
   :exists? #(exists? % db)))
