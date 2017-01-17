(ns micropress.view
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe]]
            [micropress.route :refer [current-view]]))

(defn index []
  (reagent/create-class
   {:reagent-render
    (let [route (subscribe [:route])]
      (fn []
        [:div.wrap
         [:header
          [:h2 "管理画面"]]
         [:section
          [current-view @route]]]))}))
