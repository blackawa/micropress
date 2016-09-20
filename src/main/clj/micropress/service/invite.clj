(ns micropress.service.invite
  (:require [clj-time.core :as c]
            [clj-time.format :as f]
            [micropress.repository :as r]
            [micropress.util.encrypt :as ecp]))

(defn invite
  "メールアドレスと権限IDの配列から
   ユーザーの招待情報を登録する."
  [email auth]
  (let [token (ecp/create-token-by-obj email)]
    (r/insert-invitee token email auth)))

(defn send-invite-mail
  "メンバー招待のメールを送信する."
  [email]
  (println "[mock]mailing to" email)
  ;; okの結果を返す
  true)

(defn- parse-time
  [t]
  (f/unparse (f/formatter "yyyy/MM/dd HH:mm:ss") t))

(defn view-invitees
  "招待一覧を返す"
  []
  (->> (r/find-all-invitees)
       (map #(assoc % :expire_time (parse-time (:expires_time %))))))

(defn view-invitee
  [invitee-token]
  (->> (r/find-invitee-by-token invitee-token)
       (filter #(c/before? (c/now) (:expire_time %)))
       (map #(assoc % :expire_time (parse-time (:expires_time %))))
       first))

(defn delete-invitation
  [invitee-id]
  (r/delete-invitee invitee-id))
