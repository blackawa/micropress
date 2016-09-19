(ns micropress.service.invite
  (:require [clj-time.format :as f]
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

(defn view-invitees
  "招待一覧を返す"
  []
  (->> (r/find-all-invitees)
       (map #(assoc % :expire_time (f/unparse (f/formatter "yyyy/MM/dd HH:mm:ss") (:expires_time %))))))
