(ns micropress.view
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [subscribe]]
            [micropress.route :refer [current-view]]))

(defn index []
  (reagent/create-class
   {:reagent-render
    (let [route (subscribe [:route])]
      (fn []
        [:div.ui.container
         [:nav
          (when (empty? (re-find #"^:login.*" (str (first @route))))
            [:div.ui.top.attached.tabular.menu
             (if (not (empty? (re-find #"^:article.*" (str (first @route)))))
               [:a.active.item {:href "/admin/articles"} "Articles"]
               [:a.item {:href "/admin/articles"} "Articles"])
             (if (= :profile (first @route))
               [:a.active.item {:href "/admin/profile"} "Profile"]
               [:a.item {:href "/admin/profile"} "Profile"])])
          [:section.ui.bottom.attached.segment [current-view @route]]]]))}))
