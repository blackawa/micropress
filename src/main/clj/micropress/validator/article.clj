(ns micropress.validator.article
  (:require [clj-time.core :as time]
            [clj-time.format :as f]
            [micropress.repository.article :as article]
            [micropress.validator.draft :as vd]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(defn submitted-draft?
  [article-id target]
  (let [article (article/find-by-id article-id)]
    (or (when (nil? article) (v/->result false "Article does not exist" target))
        ;; TODO: when not submitted, return validation error.
        (when (not (= 2 (:article_statuses_id article))) (v/->result false "Article is not submitted" target))
        (v/->result target))))

(defn future-date?
  "yyyyMMdd HHmmss形式の文字列が
   未来日かチェックする."
  [date target]
  (try (let [ok? (time/after? (f/parse (f/formatter "yyyyMMdd HHmmss") date)
                              (time/now))]
         (v/->result ok? (when (not ok?) "Future date is required") target))
       (catch IllegalArgumentException e
         (v/->result false "Date should be formatted by 'yyyyMMdd HHmmss'" target))))

(defn validate-publish
  "記事IDから、公開処理をかけていい記事なのかチェックする."
  [article-id publish-at]
  (v/aggregate
   (submitted-draft? article-id :article-id)
   (if (clojure.string/blank? publish-at)
     {:ok? true :messages nil}
     (future-date? publish-at :publish-at))))
