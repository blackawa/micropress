(ns micropress.util.validator
  "再利用できそうな便利なバリデータを入れておく.")

(def ^:private email-format #"^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$")
