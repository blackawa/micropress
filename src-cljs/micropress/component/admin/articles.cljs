(ns micropress.component.admin.articles
  (:require [goog.string :as string]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [markdown.core :refer [md->html]]
            [micropress.endpoint.admin.article :as article]
            [micropress.endpoint.admin.auth-token :as auth-token]
            [micropress.endpoint.admin.file :as file]))

(defn articles []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-except-route])
      (auth-token/check)
      (article/fetch-all))
    :reagent-render
    (fn []
      (let [articles (subscribe [:data])]
        [:div
         [:a.ui.green.button {:href "/admin/articles/new"} "create new article"]
         [:table.ui.celled.table
          [:thead
           [:tr [:th "id"] [:th "title"] [:th "status"]]]
          [:tbody
           (map
            (fn [a]
              [:tr
               {:key (:id a)}
               [:td (:id a)]
               [:td [:a {:href (str "/admin/articles/" (:id a))} (:title a)]]
               [:td (:article_status_id a)]])
            @articles)]]]))}))

(defn- article-form [dropdown button]
  (reagent/create-class
   {:reagent-render
    (fn []
      (let [form (subscribe [:form])
            error (subscribe [:error])
            data (subscribe [:data])
            preview-on-click (fn [preview?] (dispatch [:articles/data.preview? preview?]))]
        [:div
         [:p.error @error]
         [:form.ui.form
          [:p.field
           [:input {:type "text"
                    :id "title"
                    :name "title"
                    :placeholder "title"
                    :value (:title @form)
                    :on-change #(dispatch [:articles/form.title (-> % .-target .-value)])}]]
          [:div.field
           (when (:preview? @data)
             [:div.ui.top.attached.tabular.menu
              [:span.tab.item {:on-click #(preview-on-click false)} "raw"]
              [:span.tab.active.item {:on-click #(preview-on-click true)} "preview"]])
           (when (:preview? @data)
             [:div.ui.buttom.attached.segment
              [:div {:dangerouslySetInnerHTML {:__html (md->html (:content @form))}}]])
           (when-not (:preview? @data)
             [:div.ui.top.attached.tabular.menu
              [:span.tab.active.item {:on-click #(preview-on-click false)} "raw"]
              [:span.tab.item {:on-click #(preview-on-click true)} "preview"]])
           (when-not (:preview? @data)
             [:div.ui.bottom.attached.segment
              [:textarea {:id "content"
                          :name "content"
                          :placeholder "content"
                          :value (:content @form)
                          :on-change #(dispatch [:articles/form.content (-> % .-target .-value)])}]])]
          [:p.fields
           [:span.eight.wide.field
            [:label {:for "image"} "image"]
            [:input {:type "file"
                     :id "image"
                     :on-change #(file/upload (-> % .-target .-files (aget 0)))}]]
           [:span.four.wide.field
            [:label "save type"]
            [dropdown]]
           [:span.four.wide.field [button]]]]]))}))

(defn- new-article-dropdown []
  (let [form (subscribe [:form])]
    [:select
     {;; use (or) to suppress error to use nil for value of <select>
      :value (or (:save-type @form) "")
      :on-change #(dispatch [:articles/form.save-type (keyword (-> % .-target .-value))])}
     [:option {:value :draft} "draft"]
     [:option {:value :published} "published"]]))

(defn- new-article-button []
  (let [form (subscribe [:form])]
    [:button.ui.green.button {:on-click #(do (.preventDefault %) (article/save @form))} "create"]))

(defn new-article []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-except-route])
      (auth-token/check)
      (dispatch [:articles.new/init]))
    :reagent-render
    (fn []
      [article-form
       new-article-dropdown
       new-article-button])}))

(defn- edit-article-dropdown []
  (let [form (subscribe [:form])]
    [:select
     {;; use (or) to suppress error to use nil for value of <select>
      :value (or (:save-type @form) "")
      :on-change #(dispatch [:articles/form.save-type (keyword (-> % .-target .-value))])}
     [:option {:value :draft} "draft"]
     [:option {:value :published} "published"]
     [:option {:value :withdrawn} "withdrawn"]]))

(defn- edit-article-button []
  (let [form (subscribe [:form])]
    [:button.ui.green.button {:on-click #(do (.preventDefault %) (article/put @form))} "update"]))

(defn article []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (let [[_ id] @(subscribe [:route])]
        (dispatch [:init-except-route])
        (auth-token/check)
        (dispatch [:articles.edit/init])
        (article/fetch-by-id id)))
    :reagent-render
    (fn []
      [article-form
       edit-article-dropdown
       edit-article-button])}))
