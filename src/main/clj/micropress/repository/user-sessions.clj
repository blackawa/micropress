(ns micropress.repository.user-sessions
  (:require [clj-time.core :as c]
            [korma.core :refer [insert values]]
            [micropress.entity :as e]))

(defn insert-session
  [id token]
  (insert e/user-sessions
          (values {:users_id id :token token :expire_time (.toDate (c/now))
                   ;; clj-time.jdbcを使えば良さそう. あとオブジェクト操作が必要.
                   ;; また、c/plus で日付の操作ができる
                   })))
