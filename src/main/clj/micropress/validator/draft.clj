(ns micropress.validator.draft
  (:require [micropress.util.validator :as v]
            [micropress.validator.user :as vu]))

(defn valid-title?
  [title])

(defn valid-description?
  [description])

(defn valid-thumbnail-url?
  [url]
  [true {:msg nil :target url}])

(defn valid-body-type?
  [body-type]
  )

(defn valid-tags?
  [tags]
  )

(defn validate-save
  [{:keys [title description thumbnail-url body-type tags user-id]}]
  (v/aggregate
   (valid-title? title)
   (valid-description? description)
   (valid-thumbnail-url? thumbnail-url)
   (valid-body-type? body-type)
   (valid-tags?)
   (vu/is-active-user? user-id)))
