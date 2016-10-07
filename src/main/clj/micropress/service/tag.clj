(ns micropress.service.tag)

(defn classify-tags
  "インプットとして渡されたtagsのマップを、
   既存タグと新規タグに分類して返す.
   [{:id 1} {:keyword \"tag1\"}]のようなvecをインプットとする.
   new-tagsとexisting-tagsの順で返却する."
  [tags]
  ((juxt (partial filter #(not (nil? (:keyword %))))
                                       (partial filter #(not (nil? (:id %))))) tags))
