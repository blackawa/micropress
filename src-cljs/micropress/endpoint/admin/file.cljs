(ns micropress.endpoint.admin.file
  (:require [accountant.core :as accountant]
            [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch]]
            [micropress.endpoint.api :refer [request]]
            [micropress.util.cookie :refer [get-cookie]]))

(defn- generate-form-data [params]
  (let [form-data (js/FormData.)]
    (doseq [[k v] params]
      (if (coll? v)
        (.append form-data (name k) (first v) (second v))
        (.append form-data (name k) v)))
    form-data))

(defn upload [file]
  (if-let [auth-token (get-cookie :token)]
    (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/admin/file")
             :post
             (fn [xhrio]
               (let [file-info (-> xhrio .getResponseText read-string :file-info)]
                 ;; TODO: clear input tag when upload succeeded.
                 (dispatch [:admin.file.upload (str "![" (:file-name file-info) "](" (:file-path file-info) ")")])))
             :body
             ;; have to generate FormData to send multipart form
             (generate-form-data {:file file})
             :error-handler
             (fn [e xhrio]
               (condp = (.getStatus xhrio)
                 401 (accountant/navigate! "/admin/login")
                 (dispatch [:admin.file.upload.error "Sorry! Unexpected error occurred. Try again later..."])))
             :headers {;; you should not set "Content-Type" key when you send FormData
                       "Authorization" (str "Bearer " auth-token)})
    (accountant/navigate! "/admin/login")))
