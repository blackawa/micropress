(ns micropress.service.auth
  (:require [clj-time.core :as t]
            [korma.core :refer [select fields where]]
            [micropress.entity :as e]
            [micropress.repository :as repo]
            [micropress.util.encrypt :as ecp]))

(defn find-user
  "メアドとパスワードからユーザーを探す"
  [email pwd]
  (let [user (first (select e/users
                            (fields :id :email_address :password)
                            (where {:email_address email
                                    :user_statuses_id 1})))]
    (when (= (ecp/hash pwd) (:password user))
      (dissoc user :password))))

(defn create-token
  "ユーザーにセッショントークンを払い出す"
  [user]
  (ecp/create-token-by-obj user))

(defn validate-token
  "トークンが有効ならtrueを返却する"
  [token]
  (->> (repo/find-session token)
       (filter #(and (t/after? (:expire_time %) (t/now))
                     (pos? (t/in-seconds (t/interval (t/now) (:expire_time %))))))
       empty?
       not))
