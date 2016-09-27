(ns micropress.util.validator
  "再利用できそうな便利なバリデータを入れておく."
  (:require [schema.core :as s]))

(def email-format #"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$")
(def username #"^[a-zA-Z0-9_-]+$")
(def password (re-pattern "^[a-zA-Z0-9!@#$%^&*-_=+|\\/?<>]+$"))

(defn ->result
  "入力チェック結果生成の標準.
   入力チェック結果、エラーメッセージ、チェックしたプロパティ名を受け取って
   入力チェック結果のmapを生成する."
  [result msg target]
  {:ok? result :messages [{:target target :message msg}]})

(defn validate
  "バリデーションを実行する.
   結果を例外ではなく、[bool {:msg msg :target target}]の形式で返却する."
  ([validator target target-nm]
   (validate validator target target-nm nil))
  ([validator target target-nm err-msg]
   (try (let [target (s/validate validator target)]
          (->result true nil target-nm))
        (catch clojure.lang.ExceptionInfo e
          (->result false (if (nil? err-msg) (.getMessage e) err-msg) target-nm)))))

(defn aggregate
  "validate関数の複数の結果をマージして最終的な結果を返却する."
  [& results]
  (reduce
   (fn [r1 r2]
     {:ok? (and (:ok? r1) (:ok? r2))
      :messages (concat (when (:ok? r1) (:messages r1))
                        (when (:ok? r2) (:messages r2)))})
   results))
