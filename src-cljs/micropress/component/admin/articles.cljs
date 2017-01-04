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
      (auth-token/check)
      (article/fetch-all))
    :reagent-render
    (fn []
      (let [articles (subscribe [:admin.articles])]
        [:div
         [:h3 "articles"]
         [:a {:href "/admin/articles/new"} "create new article"]
         [:table.pure-table
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

(defn new-article []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (auth-token/check)
      (dispatch [:init-new-article-db]))
    :reagent-render
    (fn []
      (let [form (subscribe [:admin.articles.new.form])
            error (subscribe [:admin.articles.new.error])]
        [:div
         [:h3 "new article"]
         [:p.error @error]
         [:form
          [:p.title
           [:label {:for "title"} "title"]
           [:input {:type "text"
                    :id "title"
                    :name "title"
                    :placeholder "title"
                    :value (:title @form)
                    :on-change #(dispatch [:admin.articles.new.title (-> % .-target .-value)])}]]
          [:p.content
           [:label {:for "content"} "content"]
           [:textarea {:id "content"
                       :name "content"
                       :placeholder "content"
                       :value (:content @form)
                       :on-change #(dispatch [:admin.articles.new.content (-> % .-target .-value)])}]]
          [:p
           [:label {:for "image"} "image"]
           [:input {:type "file"
                    :id "image"
                    :on-change #(file/upload (-> % .-target .-files (aget 0)))}]]
          [:div.content-preview
           [:label "content-preview"]
           [:div {:dangerouslySetInnerHTML {:__html (md->html (:content @form))}}]]
          [:p.save-type
           [:label {:for "save-type"} "save-type"]
           [:select
            {;; use (or) to suppress error to use nil for value of <select>
             :value (or (:save-type @form) "")
             :on-change #(dispatch [:admin.articles.new.save-type (keyword (-> % .-target .-value))])}
            [:option {:value :draft} "draft"]
            [:option {:value :published} "published"]]]
          [:p [:button {:on-click #(do (.preventDefault %) (article/save @form))} "save"]]]]))}))

(defn article []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (let [[_ _ id] @(subscribe [:route])]
        (auth-token/check)
        (dispatch [:init-edit-article-db])
        (article/fetch-by-id id)))
    :reagent-render
    (fn []
      (let [form (subscribe [:admin.article.edit.form])
            error (subscribe [:admin.articles.edit.error])]
        [:div
         [:h3 "new article"]
         [:p.error @error]
         [:form
          [:p.title
           [:label {:for "title"} "title"]
           [:input {:type "text"
                    :id "title"
                    :name "title"
                    :placeholder "title"
                    :value (:title @form)
                    :on-change #(dispatch [:admin.articles.new.title (-> % .-target .-value)])}]]
          [:p.content
           [:label {:for "content"} "content"]
           [:textarea {:id "content"
                       :name "content"
                       :placeholder "content"
                       :value (:content @form)
                       :on-change #(dispatch [:admin.articles.new.content (-> % .-target .-value)])}]]
          [:p
           [:label {:for "image"} "image"]
           [:input {:type "file"
                    :id "image"
                    :on-change #(file/upload (-> % .-target .-files (aget 0)))}]]
          [:div.content-preview
           [:label "content-preview"]
           [:div {:dangerouslySetInnerHTML {:__html (md->html (:content @form))}}]]
          [:p.save-type
           [:label {:for "save-type"} "save-type"]
           [:select
            {;; use (or) to suppress error to use nil for value of <select>
             :value (or (:save-type @form) "")
             :on-change #(dispatch [:admin.articles.new.save-type (keyword (-> % .-target .-value))])}
            [:option {:value :draft} "draft"]
            [:option {:value :published} "published"]
            [:option {:value :withdrawn} "withdrawn"]]]
          [:p [:button {:on-click #(do (.preventDefault %) (article/put @form))} "save"]]]]))}))
