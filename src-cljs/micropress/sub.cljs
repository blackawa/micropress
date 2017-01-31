(ns micropress.sub
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :route
 (fn [db _] (:route db)))

(reg-sub
 :login.form
 (fn [db _] (:form db)))

(reg-sub
 :error
 (fn [db _] (:error db)))

(reg-sub
 :admin.articles
 (fn [db _] (:data db)))

(reg-sub
 :admin.articles.new.form
 (fn [db _] (:form db)))

(reg-sub
 :admin.articles.new.error
 (fn [db _] (:error db)))

(reg-sub
 :admin.article.edit.form
 (fn [db _] (:form db)))

(reg-sub
 :admin.articles.edit.error
 (fn [db _] (:error db)))

(reg-sub
 :customer.article
 (fn [db _] (:data db)))

(reg-sub
 :customer.articles
 (fn [db _] (:data db)))

(reg-sub
 :admin.articles.preview
 (fn [db _] (:preview? db)))

(reg-sub
 :editor.form
 (fn [db _] (:form db)))

(reg-sub
 :profile.data
 (fn [db _] (:data db)))
