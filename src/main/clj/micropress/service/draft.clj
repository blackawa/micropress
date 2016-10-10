(ns micropress.service.draft
  (:require [korma.db :as db]
            [micropress.repository.article :as article]
            [micropress.repository.article-tag :as article-tag]
            [micropress.repository.tag :as tag]
            [micropress.service.tag :as st]))

(defn find-by-id
  [article-id user-id]
  (article/find-draft-by-id article-id user-id))

(defn find-all
  [user-id]
  (article/find-all-drafts user-id))

(defn save-draft
  [{:keys [title body thumbnail-url body-type tags user-id]}]
  ;; todo トランザクションを開始して、記事の追加とタグの追加を全部終わらせたらコミットする
  (let [[new-tags existing-tags] (st/classify-tags tags)
        article-id (:generated_key (article/insert-article title body thumbnail-url body-type user-id))]
    (->> new-tags
         (map #(:keyword %))
         (tag/insert-tags)
         (concat (map #(:id %) existing-tags))
         (article-tag/insert-article-tags article-id))))

(defn submit-draft
  [article-id]
  (article/submit-draft article-id))

(defn update-draft
  [{:keys [article-id title body thumbnail-url body-type tags submit? user-id]}]
  (let [[new-tags existing-tags] (st/classify-tags tags)]
    (article/update-draft article-id title body thumbnail-url body-type submit? user-id)
    (article-tag/delete-by-article-id article-id)
    (->> new-tags
         (map #(:keyword %))
         (tag/insert-tags)
         (concat (map #(:id %) existing-tags))
         (article-tag/insert-article-tags article-id))))

(defn delete-draft
  [article-id]
  (article/delete-article article-id))
