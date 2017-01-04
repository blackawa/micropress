(ns micropress.component.not-found
  (:require [reagent.core :as reagent]))

(defn not-found []
  (reagent/create-class
   {:reagent-render
    (fn [] [:div "404 Not found..."])}))
