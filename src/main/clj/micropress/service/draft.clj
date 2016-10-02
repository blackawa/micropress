(ns micropress.service.draft
  (:require [korma.db :as db]
            [micropress.repository.article :as article]
            [micropress.repository.article-tag :as article-tag]
            [micropress.repository.tag :as tags]))

(defn save-draft
  [{:keys [title body thumbnail-url body-type tags user-id]}]
  ;; todo トランザクションを開始して、記事の追加とタグの追加を全部終わらせたらコミットする
  (let [[new-tags existing-tags] ((juxt (partial filter #(not (nil? (:keyword %))))
                                       (partial filter #(not (nil? (:id %))))) tags)
        article-id (:generated_key (article/insert-article title body thumbnail-url body-type user-id))
        inserted-tag-ids (tags/insert-tags (map #(:keyword %) new-tags))]
    (->>  new-tags
          (map #(:keyword %))
          (tags/insert-tags)
          (concat (map #(:id %) existing-tags))
          (article-tag/insert-article-tags article-id))))

(defn submit-draft
  [article-id]
  (article/submit-draft article-id))
