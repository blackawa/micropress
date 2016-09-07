(ns micropress.entity
  (:require [korma.core :refer :all]
            [korma.db :refer [defdb mysql]]))

(defdb db (mysql {:host "127.0.0.1"
                  :port 3306
                  :db "micropress"
                  :user "micropress"
                  :password "p@ssw0rd"}))

(defentity user-statuses
  (table :user_statuses))
(defentity authorities)
(defentity users
  (belongs-to user-statuses)
  (many-to-many authorities :user_authorities))
(defentity user-sessions
  (table :user_sessions)
  (belongs-to users))
(defentity invitations)
(defentity user-histories
  (table :user_histories)
  (pk [:id :user_id :updated_time]))
(defentity events)
(defentity webhooks
  (belongs-to events))
(defentity article-statuses
  (table :article_statuses))
(defentity body-types
  (table :body_types))
(defentity articles
  (belongs-to article-statuses)
  (belongs-to users))
(defentity article_status_histories
  (pk [:id :article_id :updated_time]))
