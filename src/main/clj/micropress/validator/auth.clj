(ns micropress.validator.auth
  (:require [micropress.repository :as r]
            [micropress.util.validator :as v]
            [schema.core :as s]))

(def auth-format [s/Num])

(defn valid-auth?
  "権限IDが正しいか調べる"
  [auth target]
  (let [ok? (->> auth
                 (map #(not (nil? (r/find-auth-by-id %))))
                 (reduce (fn [b1 b2] (and b1 b2))))]
    (v/->result ok? (when (not ok?) "Contains invalid auth.") target)))
