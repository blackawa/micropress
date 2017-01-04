(ns oyacolab.component.customer.index
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [oyacolab.endpoint.customer.article :as article]))

(defn index []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-customer-articles-db])
      (article/fetch-all))
    :reagent-render
    (fn []
      (let [articles (subscribe [:customer.articles])]
        [:div
         [:ul
          (map
           (fn [a]
             [:li
              {:key (:id a)}
              [:a {:href (str "/articles/" (:id a))} (:title a)]])
           @articles)]]))}))
