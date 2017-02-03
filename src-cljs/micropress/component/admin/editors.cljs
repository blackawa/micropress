(ns micropress.component.admin.editors
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [micropress.endpoint.admin.auth-token :as auth-token]
            [micropress.endpoint.admin.editor :as editor]))

(defn editors []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-except-route])
      (auth-token/check)
      (editor/fetch-all))
    :reagent-render
    (fn []
      (let [data (subscribe [:data])]
        [:div
         [:a.ui.green.button {:href "/admin/editors/new"} "invite new editor"]
         (when @data
           [:table.ui.celled.table
            [:thead
             [:tr [:th "id"] [:th "username"] [:th "status"]]]
            [:tbody
             (map
              (fn [e]
                [:tr
                 {:key (:id e)}
                 [:td (:id e)]
                 [:td (:username e)]
                 [:td (:editor_status_id e)]])
              @data)]])]))}))

(defn new-editor []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-except-route])
      (auth-token/check))
    :reagent-render
    (fn []
      [:div
       [:button.ui.green.button "publish invite code"]
       [:ul
        [:li "http://hogehoge.com/invitation/1234-5678"]]])}))
