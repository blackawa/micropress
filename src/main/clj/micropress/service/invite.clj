(ns micropress.service.invite
  (:require [clj-time.core :as c]
            [clj-time.format :as f]
            [micropress.repository.invitee :as invitee]
            [micropress.util.encrypt :as ecp]
            [micropress.util.time :as t]))

(defn invite
  "メールアドレスと権限IDの配列から
   ユーザーの招待情報を登録する."
  [email auth]
  (let [token (ecp/create-token-by-obj email)]
    (invitee/insert-invitee token email auth)))

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
  (->> (invitee/find-all)
       (map #(assoc % :expire_time (t/time->string (:expire_time %))))))

(defn view-invitee
  [invitee-token]
  (->> (invitee/find-by-token invitee-token)
       (filter #(c/before? (c/now) (:expire_time %)))
       (map #(assoc % :expire_time (t/time->string (:expire_time %))))
       first))

(defn delete-invitation
  [invitee-id]
  (invitee/delete-by-id invitee-id))
