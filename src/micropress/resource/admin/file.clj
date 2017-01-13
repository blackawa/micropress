(ns micropress.resource.admin.file
  (:require [clj-time.core :as time]
            [clj-time.jdbc]
            [clojure.java.io :as io]
            [environ.core :refer [env]]
            [liberator.core :refer [resource]]
            [liberator.representation :refer [ring-response]]
            [micropress.repository.auth-token :as token]
            [micropress.repository.file-storage :as file-storage]
            [micropress.resource.base :refer [authenticated]]))

(defn- generate-unique-filename [content-type]
  (let [file-name (str (java.util.UUID/randomUUID))
        extension (condp #(clojure.string/includes? %2 %1) content-type
                    "png" ".png"
                    "jpg" ".jpg"
                    "")]
    (str file-name extension)))

(defn- post! [ctx db]
  (let [{:keys [filename content-type tempfile size]} (-> ctx (get-in [:request :multipart-params "file"]))
        upload-filename (generate-unique-filename content-type)]
    ;; program for debug: (io/copy tempfile (io/file (format "./target/%s" upload-filename)))
    (file-storage/save upload-filename tempfile)
    {::file-info {:file-path (str "https://s3.amazonaws.com/" (:bucket-name env) "/" upload-filename)
                  :file-name filename}}))

(defn- handle-created [ctx]
  (let [file-info (::file-info ctx)]
    (ring-response {:body (str {:file-info file-info})})))

(defn file [db]
  (resource
   (authenticated db)
   :allowed-methods [:post]
   :available-media-types ["multipart/form-data"]
   :post! #(post! % db)
   :handle-created handle-created))
