(ns micropress.validator.auth
  (:require [micropress.repository :as r]
            [schema.core :as s]))

(def auth-format [s/Num])

(defn valid-auth?
  "権限IDが正しいか調べる"
  [auth]
  (let [ok? (->> auth
                 (map #(not (nil? (r/find-auth-by-id %))))
                 (reduce (fn [b1 b2] (and b1 b2))))]
    (if ok?
      [true {:msg nil :target auth}]
      [false {:msg "Contains invalid auth."} :target auth])))
