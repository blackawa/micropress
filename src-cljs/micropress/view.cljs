(ns micropress.view
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe]]
            [micropress.route :refer [current-base-view]]))

(defn index []
  (reagent/create-class
   {:reagent-render
    (let [route (subscribe [:route])]
      (fn []
        [current-base-view @route]))}))
