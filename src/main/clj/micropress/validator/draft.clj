(ns micropress.validator.draft
  (:require [micropress.repository :as r]
            [micropress.util.validator :as v]
            [micropress.validator.user :as vu]
            [schema.core :as s]))

(defn valid-title?
  [title]
  (let [[title-str? _] (v/validate s/Str title)
        title-length-ok? (and (< 0 (count title)) (< (count title) 256))
        ok? (and title-str? title-length-ok?)]
    [ok? {:msg (when (not ok?) "Title should have 1 - 128 characters.") :target title}]))

(defn valid-description?
  [description]
  (let [[desc-str? _] (v/validate s/Str description)
        desc-length-ok? (< 0 (count description))
        ok? (and desc-str? desc-length-ok?)]
    [ok? {:msg (when (not ok?) "Title should have more than 1 characters.") :target description}]))

(defn valid-thumbnail-url?
  [url]
  [true {:msg nil :target url}]) ;; todo implement me...

(defn valid-body-type?
  [id]
  (let [ok? (not (nil? (r/find-body-type-by-id id)))]
    [ok? {:msg (when (not ok?) "Invalid body type.") :target id}]))

(defn- valid-new-tag?
  "Input: tag / Output: bool"
  [{:keys [keyword] :as tag}]
  (let [[tag-str? _] (v/validate s/Str tag)
        tag-length-ok? (and (< 0 (count tag)) (< (count tag) 128))
        ok? (and tag-str? tag-length-ok?)]
    [ok? {:msg (when (not ok?) "Invalid new tag") :target tag}]))

(defn- valid-existing-tag?
  "Input: tag / Output: bool"
  [tag])

(defn valid-tags?
  "[{:keyword 'hoge' :id nil} {:type :existing :id 1}]
   みたいな形を想定する."
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
