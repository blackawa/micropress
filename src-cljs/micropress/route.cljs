(ns micropress.route
  (:require [secretary.core :refer-macros [defroute]]
            [re-frame.core :refer [dispatch subscribe]]
            [reagent.core :as reagent]
            [micropress.component.not-found :as not-found]
            [micropress.component.admin.login :as admin-login]
            [micropress.component.admin.articles :as admin-articles]))

;; url ===> route ============================
(defroute "/" []
  (dispatch [:route [:admin :admin.login]]))
(defroute "/admin/login" []
  (dispatch [:route [:admin :admin.login]]))
(defroute "/admin/articles" []
  (dispatch [:route [:admin :admin.articles]]))
(defroute "/admin/articles/new" []
  (dispatch [:route [:admin :admin.articles.new]]))
(defroute "/admin/articles/:id" [id]
  (dispatch [:route [:admin :admin.article id]]))

;; route ===> view ===========================
;; inner-view ================================
(defmulti current-view #(second %))
(defmethod current-view :admin.login []
  [admin-login/login])
(defmethod current-view :admin.articles []
  [admin-articles/articles])
(defmethod current-view :admin.articles.new []
  [admin-articles/new-article])
(defmethod current-view :admin.article []
  [admin-articles/article])
(defmethod current-view :default []
  [not-found/not-found])

;; base-view =================================
(defn- admin []
  (reagent/create-class
   {:reagent-render
    (let [route (subscribe [:route])]
      (fn []
        [:div.wrap
         [:header
          [:h2 "管理画面"]]
         [:section
          [current-view @route]]]))}))
(defmulti current-base-view #(first %))
(defmethod current-base-view :admin []
  [admin])
(defmethod current-base-view :default []
  [(fn [] [:div])])
