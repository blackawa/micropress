(ns micropress.service.auth
  (:require [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [korma.core :refer [select fields where]]
            [micropress.entity :as e]))

(defn- pwd->hash
  "SHA256ハッシュ値に変換する"
  [row-pwd]
  (-> (hash/sha256 row-pwd)
      (bytes->hex)))

(defn authenticate
  [email pwd]
  (letfn [(test [pwd user] (when (= (pwd->hash pwd) (:password user))
                             (:id user)))]
    (->> (select e/users
                 (fields :id :email_address :password)
                 (where {:email_address email
                         :user_statuses_id 1}))
         first
         (test pwd))))
