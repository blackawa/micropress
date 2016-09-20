(ns micropress.util.time
  (:require [clj-time.format :as f]))

(defn time->string
  [t]
  (f/unparse (f/formatter "yyyy/MM/dd HH:mm:ss") t))
