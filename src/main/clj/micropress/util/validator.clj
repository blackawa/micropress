(ns micropress.util.validator
  "再利用できそうな便利なバリデータを入れておく."
  (:require [schema.core :as s]))

(def email-format #"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$")
(def username #"^[a-zA-Z0-9_-]+$")
(def password (re-pattern "^[a-zA-Z0-9!@#$%^&*-_=+|\\/?<>]+$"))

(defn validate
  "バリデーションを実行する.
   結果を例外ではなく、[bool {:msg msg :target target}]の形式で返却する."
  ([validator target]
   (validate validator target nil))
  ([validator target err-msg]
   (try (let [target (s/validate validator target)]
          [true {:msg nil :target target}])
        (catch clojure.lang.ExceptionInfo e
          [false
           {:msg (if (nil? err-msg) (.getMessage e) err-msg)
            :target target}]))))

(defn aggregate
  "validate関数の複数の結果をマージして最終的な結果を返却する."
  [& results]
  (reduce
   (fn [target src]
     [(and (first target) (first src))
      (filter #(not (nil? %)) (cons (:msg (second src)) (second target)))])
   (vector true)
   results))
