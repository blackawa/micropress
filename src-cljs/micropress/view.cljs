(ns micropress.view
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe]]
            [micropress.route :refer [current-view]]))

(defn index []
  (reagent/create-class
   {:reagent-render
    (let [route (subscribe [:route])]
      (fn []
        [:div
         [:header.ui.fixed.inverted.teal.menu
          [:a.ui.inverted.header.item {:href "/"} "console | micropress"]
          (when (not (= :login (first @route)))
            (if (not (empty? (re-find #"^:article.*" (str (first @route)))))
              [:a.active.item {:href "/admin/articles"} "Articles"]
              [:a.item {:href "/admin/articles"} "Articles"]))
          (when (not (= :login (first @route)))
            (if (not (empty? (re-find #"^:profile.*" (str (first @route)))))
              [:a.active.item {:href "/admin/profile"} "Profile"]
              [:a.item {:href "/admin/profile"} "Profile"]))]
         [:div.ui.container
          [current-view @route]]]))}))
