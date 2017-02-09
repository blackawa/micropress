(ns micropress.route
  (:require [secretary.core :refer-macros [defroute]]
            [re-frame.core :refer [dispatch]]
            [reagent.core :as reagent]
            [micropress.component.not-found :refer [not-found]]
            [micropress.component.admin.login :refer [login]]
            [micropress.component.public.invitation :refer [invitation]]
            [micropress.component.admin.articles :as articles]
            [micropress.component.admin.editors :refer [editors new-editor]]
            [micropress.component.admin.profile :refer [profile]]))

;; url ===> route ============================
(defroute "/" []
  (dispatch [:route [:login]]))
(defroute "/login" []
  (dispatch [:route [:login]]))
(defroute "/invitation/:token" [token]
  (dispatch [:route [:invitation token]]))
(defroute "/admin/articles" []
  (dispatch [:route [:articles]]))
(defroute "/admin/articles/new" []
  (dispatch [:route [:articles.new]]))
(defroute "/admin/articles/:id" [id]
  (dispatch [:route [:article id]]))
(defroute "/admin/editors" []
  (dispatch [:route [:editors]]))
(defroute "/admin/editors/new" []
  (dispatch [:route [:editors.new]]))
(defroute "/admin/profile" []
  (dispatch [:route [:profile]]))

;; route ===> view ===========================
;; inner-view ================================
(defmulti current-view first)
(defmethod current-view :login []
  [login])
(defmethod current-view :invitation []
  [invitation])
(defmethod current-view :articles []
  [articles/articles])
(defmethod current-view :articles.new []
  [articles/new-article])
(defmethod current-view :article []
  [articles/article])
(defmethod current-view :editors []
  [editors])
(defmethod current-view :editors.new []
  [new-editor])
(defmethod current-view :profile []
  [profile])
(defmethod current-view :not-found []
  [not-found])
(defmethod current-view :default []
  [:div])
