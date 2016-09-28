(ns micropress.service.auth
  (:require [clj-time.core :as t]
            [micropress.repository.session :as s]
            [micropress.repository.user :as user]
            [micropress.util.encrypt :as ecp]))

(defn find-user
  "メアドとパスワードからユーザーを探す"
  [email pwd]
  (let [user (user/find-active-by-email email)]
    (when (= (ecp/hash pwd) (:password user))
      (dissoc user :password))))

(defn create-token
  "ユーザーにセッショントークンを払い出す"
  [user]
  (ecp/create-token-by-obj user))

(defn validate-token
  "トークンが有効ならtrueを返却する"
  [token]
  (->> (s/find-by-token token)
       (filter #(and (t/after? (:expire_time %) (t/now))
                     (pos? (t/in-seconds (t/interval (t/now) (:expire_time %))))))
       empty?
       not))
