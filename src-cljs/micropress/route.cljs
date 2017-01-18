(ns micropress.route
  (:require [secretary.core :refer-macros [defroute]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            [micropress.component.not-found :as not-found]
            [micropress.component.admin.login :as admin-login]
            [micropress.component.admin.articles :as admin-articles]
            [micropress.component.admin.profile :refer [profile]]))

;; url ===> route ============================
(defroute "/" []
  (dispatch [:route [:login]]))
(defroute "/admin/login" []
  (dispatch [:route [:login]]))
(defroute "/admin/articles" []
  (dispatch [:route [:articles]]))
(defroute "/admin/articles/new" []
  (dispatch [:route [:articles.new]]))
(defroute "/admin/articles/:id" [id]
  (dispatch [:route [:article id]]))
(defroute "/admin/profile" []
  (dispatch [:route [:profile]]))

;; route ===> view ===========================
;; inner-view ================================
(defmulti current-view first)
(defmethod current-view :login []
  [admin-login/login])
(defmethod current-view :articles []
  [admin-articles/articles])
(defmethod current-view :articles.new []
  [admin-articles/new-article])
(defmethod current-view :article []
  [admin-articles/article])
(defmethod current-view :profile []
  [profile])
(defmethod current-view :default []
  [not-found/not-found])
