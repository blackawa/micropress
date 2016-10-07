(ns micropress.validator.draft
  (:require [micropress.repository.article :as article]
            [micropress.repository.body-type :as body-type]
            [micropress.repository.tag :as tag]
            [micropress.util.validator :as v]
            [micropress.validator.user :as vu]
            [schema.core :as s]))

(defn valid-title?
  [title target]
  (let [result (v/validate s/Str title target)
        title-length-ok? (and (< 0 (count title)) (< (count title) 256))
        ok? (and (:ok? result) title-length-ok?)]
    (v/->result ok? (when (not ok?) "Title should have 1 - 128 characters.") target)))

(defn valid-body?
  [body target]
  (let [result (v/validate s/Str body target)
        body-length-ok? (< 0 (count body))
        ok? (and (:ok? result) body-length-ok?)]
    (v/->result ok? (when (not ok?) "Body should have more than 1 characters.") target)))

(defn valid-thumbnail-url?
  [thumbnail-url target]
  (v/->result true nil target)) ;; todo implement me...

(defn valid-body-type?
  [id target]
  (let [ok? (not (nil? (body-type/find-by-id id)))]
    (v/->result ok? (when (not ok?) "Invalid body type.") target)))

(defn- valid-new-tag?
  "Input: tag / Output: bool"
  [tag target]
  (let [keyword (:keyword tag)
        result (v/validate s/Str keyword target)
        tag-length-ok? (and (< 0 (count keyword)) (< (count keyword) 128))
        ok? (and (:ok? result) tag-length-ok?)]
    (v/->result ok? (when (not ok?) "Invalid new tag") target)))

(defn- valid-existing-tag?
  "Input: tag / Output: bool"
  [tag target]
  (let [ok? (not (nil? (tag/find-by-id (:id tag))))]
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

(defn draft-exist?
  [article-id user-id target]
  (let [article (article/find-by-id article-id)]
    (or (when (nil? article) (v/->result false "Article does not exist" target))
        (when (not (= user-id (:users_id article))) (v/->result false "Article is not exist" target))
        (when (not (= 1 (:article_statuses_id article))) (v/->result false "Article is not draft" target))
        (v/->result target))))

(defn validate-save
  [{:keys [title body thumbnail-url body-type tags user-id]}]
  (v/aggregate
   (valid-title? title :title)
   (valid-body? body :body)
   (valid-thumbnail-url? thumbnail-url :thumbnail-url)
   (valid-body-type? body-type :body-type)
   (valid-tags? tags :tags)
   (vu/is-active-user? user-id :user-id)))

(defn valid-submit-flag?
  [submit? target]
  (v/validate s/Bool submit? target "Invalid submit flag"))

(defn validate-submit
  [article-id user-id]
  (v/aggregate
   (draft-exist? article-id user-id :article-id)
   (vu/is-active-user? user-id :user-id)))

(defn validate-update
  [{:keys [article-id title body thumbnail-url body-type tags submit? user-id]}]
  (v/aggregate
   (draft-exist? article-id user-id :article-id)
   (valid-title? title :title)
   (valid-body? body :body)
   (valid-thumbnail-url? thumbnail-url :thumbnail-url)
   (valid-body-type? body-type :body-type)
   (valid-tags? tags :tags)
   (vu/is-active-user? user-id :user-id)
   (valid-submit-flag? submit? :submit?)))
