(ns micropress.main
  (:require [accountant.core :as accountant]
            [secretary.core :as secretary]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch-sync dispatch]]
            [micropress.config :as config]
            [micropress.view :as view]
            [micropress.event]
            [micropress.sub]
            [micropress.route]))

(defn mount-root []
  (reagent/render [view/index]
                  (js/document.getElementById "app")))

(defn ^:export init []
  (dispatch-sync [:init])
  (accountant/configure-navigation!
   {:nav-handler (fn [path] (secretary/dispatch! path))
    :path-exists? (fn [path] (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
