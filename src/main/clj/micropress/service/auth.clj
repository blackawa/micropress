(ns micropress.service.auth
  (:require [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [clj-time.coerce :as c]
            [clj-time.core :as t]
            [korma.core :refer [select fields where]]
            [micropress.entity :as e]
            [micropress.repository :as repo]))

(defn- hash
  "SHA256ハッシュ値に変換する"
  [row-pwd]
  (-> (hash/sha256 row-pwd)
      (bytes->hex)))

(defn find-user
  "メアドとパスワードからユーザーを探す"
  [email pwd]
  (let [user (first (select e/users
                            (fields :id :email_address :password)
                            (where {:email_address email
                                    :user_statuses_id 1})))]
    (if (= (hash pwd) (:password user))
      [true (dissoc user :password)]
      [false {:message "Invalid username or password."}])))

(defn create-token
  "ユーザーにセッショントークンを払い出す"
  [user]
  (-> (t/now)
      c/to-long
      (str (:id user))
      hash))

(defn validate-token
  "トークンが有効ならtrueを返却する"
  [token]
  (->> (repo/find-session token)
       (filter #(pos? (t/in-seconds (t/interval (t/now) (:expire_time %)))))
       empty?
       not))
