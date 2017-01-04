(ns micropress.event
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]))

(reg-event-fx
 :init
 (fn [_ _]
   ;; === Schema Definition ==================================
   ;; {:form {} :error {} :route {} :data {}}
   ;; ========================================================
   {}))

(reg-event-db
 :init-login-db
 (fn [db _]
   (-> db
       (assoc :form {})
       (assoc :error {}))))

(reg-event-db
 :route
 (fn [db [_ route]]
   (assoc db :route route)))

(reg-event-db
 :login.form.username
 (fn [db [_ username]]
   (assoc-in db [:form :username] username)))

(reg-event-db
 :login.form.password
 (fn [db [_ password]]
   (assoc-in db [:form :password] password)))

(reg-event-db
 :error
 (fn [db [_ error]]
   (assoc db :error error)))

(reg-event-db
 :admin.articles
 (fn [db [_ articles]]
   (assoc db :data articles)))

(reg-event-db
 :init-new-article-db
 (fn [db _]
   (-> db
       (assoc :form {:save-type :draft})
       (assoc :error {}))))

(reg-event-db
 :admin.articles.new.title
 (fn [db [_ title]]
   (assoc-in db [:form :title] title)))

(reg-event-db
 :admin.articles.new.content
 (fn [db [_ content]]
   (assoc-in db [:form :content] content)))

(reg-event-db
 :admin.articles.new.save-type
 (fn [db [_ save-type]]
   (assoc-in db [:form :save-type] save-type)))

(reg-event-db
 :init-edit-article-db
 (fn [db _]
   (-> db
       (assoc :form {})
       (assoc :error {}))))

(reg-event-db
 :admin.article
 (fn [db [_ article]]
   (assoc db :form article)))

(reg-event-db
 :admin.articles.edit.error
 (fn [db [_ error]]
   (assoc db :error error)))

(reg-event-db
 :init-customer-article-db
 (fn [db _]
   (-> db
       (assoc :data {})
       (assoc :form {})
       (assoc :error {}))))

(reg-event-db
 :customer.article
 (fn [db [_ article]]
   (assoc db :data article)))

(reg-event-db
 :init-customer-articles-db
 (fn [db _]
   (-> db
       (assoc :data {})
       (assoc :form {})
       (assoc :error {}))))

(reg-event-db
 :customer.articles
 (fn [db [_ articles]]
   (assoc db :data articles)))

(reg-event-db
 :admin.file.upload
 (fn [db [_ file-info]]
   (update-in db [:form :content] #(str % "\n\n" file-info))))

(reg-event-db
 :admin.file.upload.error
 (fn [db [_ error]]
   (assoc db [:error] error)))
