(ns oyacolab.resource.editor
  (:require [buddy.hashers :as h]
            [liberator.core :refer [defresource]]
            [oyacolab.repository.editor :as editor]))

(defn- post! [ctx db]
  (let [editor (-> ctx (get-in [:request :body]) slurp)
        entity (-> editor
                   read-string
                   (update :password h/encrypt)
                   (assoc :editor_status_id 1))]
    (editor/create-editor! entity {:connection db})))

(defresource editor [db]
  :allowed-methods [:post]
  :available-media-types ["application/edn"]
  :post! (fn [ctx] (post! ctx db)))
