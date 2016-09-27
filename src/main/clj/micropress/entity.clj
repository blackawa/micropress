(ns micropress.entity
  (:require [korma.core :refer [defentity table belongs-to many-to-many pk]]
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
(defentity user-authorities
  (table :user_authorities)) ;; 本当は定義したくないが、そうしないとINSERTする方法が思いつかない.
(defentity user-sessions
  (table :user_sessions)
  (belongs-to users))
(defentity invitees
  (many-to-many authorities :invitee_authorities))
(defentity invitee-authorities
  (table :invitee_authorities)) ;; 本当は定義したくないが、そうしないとINSERTする方法が思いつかない.
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
(defentity tags
  (many-to-many articles :articles_tag))
(defentity article-histories
  (table :article_histories)
  (many-to-many tags :articles_tag)
  (pk [:id :articles_id :updated_time]))
(defentity articles-tag
  (table :articles_tag))
