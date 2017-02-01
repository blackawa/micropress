(ns micropress.route
  (:require [secretary.core :refer-macros [defroute]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            [micropress.component.not-found :refer [not-found]]
            [micropress.component.admin.login :refer [login]]
            [micropress.component.admin.articles :as articles]
            [micropress.component.admin.editors :refer [editors]]
            [micropress.component.admin.profile :refer [profile]]))

;; url ===> route ============================
(defroute "/" []
  (dispatch [:route [:login]]))
(defroute "/login" []
  (dispatch [:route [:login]]))
(defroute "/admin/articles" []
  (dispatch [:route [:articles]]))
(defroute "/admin/articles/new" []
  (dispatch [:route [:articles.new]]))
(defroute "/admin/articles/:id" [id]
  (dispatch [:route [:article id]]))
(defroute "/admin/editors" []
  (dispatch [:route [:editors]]))
(defroute "/admin/profile" []
  (dispatch [:route [:profile]]))

;; route ===> view ===========================
;; inner-view ================================
(defmulti current-view first)
(defmethod current-view :login []
  [login])
(defmethod current-view :articles []
  [articles/articles])
(defmethod current-view :articles.new []
  [articles/new-article])
(defmethod current-view :article []
  [articles/article])
(defmethod current-view :editors []
  [editors])
(defmethod current-view :profile []
  [profile])
(defmethod current-view :not-found []
  [not-found])
(defmethod current-view :default []
  [:div])
