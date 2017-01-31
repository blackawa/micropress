(ns micropress.component.admin.profile
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [micropress.endpoint.admin.auth-token :as auth-token]
            [micropress.endpoint.admin.editor :as editor]))

(defn profile []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-except-route])
      (auth-token/check)
      (editor/fetch-by-token))
    :reagent-render
    (fn []
      (let [form (subscribe [:form])
            error (subscribe [:error])
            data (subscribe [:data])]
        [:div
         [:p.error (:error @error)]
         [:form.ui.form
          [:p.inline.field
           [:label "new password"]
           [:input {:type "password"
                    :placeholder "password"
                    :value (:password @form)
                    :on-change #(dispatch [:profile/form.password (-> % .-target .-value)])}]]
          [:button.ui.green.button {:type "submit" :on-click #(do (.preventDefault %) (editor/put(:id @data) @form))} "update"]]]))}))
