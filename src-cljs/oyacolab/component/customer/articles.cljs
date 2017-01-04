(ns oyacolab.component.customer.articles
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe dispatch]]
            [markdown.core :refer [md->html]]
            [oyacolab.endpoint.customer.article :as article]))

(defn article []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (let [[_ _ id] @(subscribe [:route])]
        (dispatch [:init-customer-article-db])
        (article/fetch-by-id id)))
    :reagent-render
    (fn []
      (let [article (subscribe [:customer.article])]
        [:article
         [:h3 (:title @article)]
         [:p (:published_date @article)]
         [:div {:dangerouslySetInnerHTML {:__html (md->html (:content @article))}}]]))}))
