(ns micropress.component.not-found
  (:require [reagent.core :as reagent]))

(defn not-found []
  (reagent/create-class
   {:reagent-render
    (fn [] [:div
            [:h1 "Page not found!"]
            [:a {:href "/"} "Restart from top page."]])}))
