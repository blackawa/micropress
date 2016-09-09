(ns micropress.service.invite
  (:require [micropress.repository :as repo]
            [micropress.util.encrypt :as ecp]))

(defn invite
  "メールアドレスと権限IDの配列から
   ユーザーの招待情報を登録する."
  [email auth]
  (let [token (ecp/create-token-by-obj email)]
    (repo/insert-invitee token email)))

(defn send-invite-mail
  "メンバー招待のメールを送信する."
  [email])
