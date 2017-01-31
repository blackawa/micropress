(ns micropress.event
  (:require [re-frame.core :refer [reg-event-fx reg-event-db]]))

;; === Schema Definition ==================================
;; {:route {} # routing
;;  :data  {} # data to display
;;  :form  {} # form data
;;  :error {} # error response
;;  :flush {:success []   # success message
;;          :failure []}} # failure message
;; ========================================================

;; initialization
(reg-event-fx :init              (fn [_ _] {}))
(reg-event-db :init-except-route (fn [db _] (select-keys db [:route])))
(reg-event-db :init-form         (fn [db _] (-> db
                                                (assoc :form nil)
                                                (assoc :error nil))))

;; submit
(reg-event-db :route (fn [db [_ route]] (assoc db :route route)))
(reg-event-db :data  (fn [db [_ data]]  (assoc db :data data)))
(reg-event-db :form  (fn [db [_ form]]  (assoc db :form form)))
(reg-event-db :error (fn [db [_ error]] (assoc db :error error)))
(reg-event-db :flush (fn [db [_ flush]] (assoc db :flush flush)))

;; component specific submit events
(reg-event-db :login/form.username (fn [db [_ username]] (assoc-in db [:form :username] username)))
(reg-event-db :login/form.password (fn [db [_ password]] (assoc-in db [:form :password] password)))

(reg-event-db :articles.new/init  (fn [db _] (-> db
                                                 (assoc :form {:save-type :draft})
                                                 (assoc :error {})
                                                 ;; TODO: change data place (prefer inside data)
                                                 (assoc :preview? false))))
(reg-event-db :articles.edit/init (fn [db _] (-> db
                                                 (assoc :form {})
                                                 (assoc :error {})
                                                 ;; TODO: change data place (prefer inside data)
                                                 (assoc :preview? false))))

(reg-event-db :articles/form.title     (fn [db [_ title]] (assoc-in db [:form :title] title)))
(reg-event-db :articles/form.content   (fn [db [_ content]] (assoc-in db [:form :content] content)))
(reg-event-db :articles/form.save-type (fn [db [_ save-type]] (assoc-in db [:form :save-type] save-type)))
;; TODO: change data place (prefer inside data)
(reg-event-db :articles/preview        (fn [db [_ preview?]] (assoc db :preview? preview?)))
(reg-event-db :articles/form.file      (fn [db [_ file-info]] (update-in db [:form :content] #(str % "\n\n" file-info))))

(reg-event-db :profile/form.password (fn [db [_ password]] (assoc-in db [:form :password] password)))
