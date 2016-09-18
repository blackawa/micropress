(ns micropress.util.encrypt
  (:require [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [clj-time.coerce :as c]
            [clj-time.core :as t]))

(defn hash
  "SHA256ハッシュ値に変換する"
  [row-pwd]
  (-> (hash/sha256 row-pwd)
      (bytes->hex)))

(defn create-token-by-obj
  "与えられたオブジェクトを文字列化し、
   現在時刻の文字列を連結してハッシュ化する"
  [obj]
  (-> (t/now)
      c/to-long
      (str obj)
      hash))
