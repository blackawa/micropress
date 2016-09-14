(ns micropress.util.validator
  "再利用できそうな便利なバリデータを入れておく."
  (:require [schema.core :as s]))

(def email-format #"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$")

(defn validate
  "バリデーションを実行する.
   結果を例外ではなく、[bool msg]の形式で返却する."
  [validator target]
  (try (let [target (s/validate validator target)]
         [true {:message nil :targe target}])
       (catch clojure.lang.ExceptionInfo e [false {:message (.getMessage e) :target target}])))
