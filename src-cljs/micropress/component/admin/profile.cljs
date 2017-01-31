(ns micropress.component.admin.profile
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [micropress.endpoint.admin.auth-token :as auth-token]
            [micropress.endpoint.admin.editor :as editor]))

(defn profile []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (auth-token/check)
      (dispatch [:init-except-route])
      (editor/fetch-by-token))
    :reagent-render
    (fn []
      (let [form (subscribe [:editor.form])
            error (subscribe [:error])
            data (subscribe [:profile.data])]
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
