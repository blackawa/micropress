(ns micropress.service.join
  (:require [clj-time.core :as c]
            [clj-time.format :as f]
            [micropress.repository :as r]
            [micropress.util.encrypt :as ecp]))

(defn accpet-invitation
  [token username nickname password]
  ;; 招待の削除
  ;; ユーザー追加
  ;; 権限の移し替え
  )
