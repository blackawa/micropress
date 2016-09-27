(ns micropress.validator.draft
  (:require [micropress.repository :as r]
            [micropress.util.validator :as v]
            [micropress.validator.user :as vu]
            [schema.core :as s]))

(defn valid-title?
  [title target]
  (let [{:keys [result]} (v/validate s/Str title target)
        title-length-ok? (and (< 0 (count title)) (< (count title) 256))
        ok? (and result title-length-ok?)]
    (v/->result ok? (when (not ok?) "Title should have 1 - 128 characters.") target)))

(defn valid-description?
  [description target]
  (let [{:keys [result]} (v/validate s/Str description target)
        desc-length-ok? (< 0 (count description))
        ok? (and result desc-length-ok?)]
    (v/->result ok? (when (not ok?) "Title should have more than 1 characters.") target)))

(defn valid-thumbnail-url?
  [thumbnail-url]
  (v/->result true nil :thumbnail-url)) ;; todo implement me...

(defn valid-body-type?
  [id target]
  (let [ok? (not (nil? (r/find-body-type-by-id id)))]
    (v/->result ok? (when (not ok?) "Invalid body type.") target)))

(defn- valid-new-tag?
  "Input: tag / Output: bool"
  [tag target]
  (let [keyword (:keyword tag)
        [tag-str? _] (v/validate s/Str tag target)
        tag-length-ok? (and (< 0 (count tag)) (< (count tag) 128))
        ok? (and tag-str? tag-length-ok?)]
    (v/->result ok? (when (not ok?) "Invalid new tag") target)))

(defn- valid-existing-tag?
  "Input: tag / Output: bool"
  [tag target]
  (let [ok? (not (nil? (r/find-tag-by-id (:id tag))))]
    (v/->result ok? (when (not ok?) "Invalid tag") target)))

(defn valid-tags?
  "[{:keyword 'hoge' :id nil} {:keyword nil :id 1}]
   みたいな形を想定する."
  [tags target]
  (->> tags
       (map #(cond (not (nil? (:id %))) (valid-existing-tag? % target)
                   (not (nil? (:keyword %))) (valid-new-tag? % target)
                   :else (v/->result false "Invalid tag format" target)))
       (apply v/aggregate)))

(defn validate-save
  [{:keys [title description thumbnail-url body-type tags user-id]}]
  (v/aggregate
   (valid-title? title :title)
   (valid-description? description :description)
   (valid-thumbnail-url? thumbnail-url :thumbnail-url)
   (valid-body-type? body-type :body-type)
   (valid-tags? :tags)
   (vu/is-active-user? user-id :user-id)))
