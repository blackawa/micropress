(ns oyacolab.resource.admin.file
  (:require [clj-time.core :as time]
            [clj-time.jdbc]
            [clojure.java.io :as io]
            [liberator.core :refer [defresource]]
            [liberator.representation :refer [ring-response]]
            [oyacolab.repository.auth-token :as token]
            [oyacolab.repository.file-storage :as file-storage]))

(defn- malformed? [ctx]
  (let [authorization (-> ctx (get-in [:request :headers]) (get "authorization"))]
    (if-let [auth-token (second (clojure.string/split authorization #"\s"))]
      [false {::data auth-token}]
      [true {::error "invalid auth token"}])))

(defn- handle-malformed [ctx]
  (let [error (::error ctx)]
    (str {:error error})))

(defn- authorized? [ctx db]
  (let [auth-token (first (token/find-by-token {:token (::data ctx)} {:connection db}))]
    (time/before? (time/now) (:expire auth-token))))

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
    {::file-info {:file-path (str "https://s3.amazonaws.com/oyacolab/" upload-filename)
                  :file-name filename}}))

(defn- handle-created [ctx]
  (let [file-info (::file-info ctx)]
    (ring-response {:body (str {:file-info file-info})})))

(defresource file [db]
  :allowed-methods [:post]
  :available-media-types ["multipart/form-data"]
  :malformed? malformed?
  :handle-malformed handle-malformed
  :authorized? #(authorized? % db)
  :post! #(post! % db)
  :handle-created handle-created)
