(ns micropress.component.admin.editors
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [micropress.endpoint.admin.auth-token :as auth-token]
            [micropress.endpoint.admin.editor :as editor]))

(defn editors []
  (reagent/create-class
   {:reagent-render
    (fn [] [:div "Editors list"])}))
