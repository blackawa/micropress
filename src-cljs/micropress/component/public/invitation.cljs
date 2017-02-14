(ns micropress.component.public$.invitation
  (:require [accountant.core :as accountant]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [micropress.endpoint.admin.auth-token :as auth-token]
            [micropress.endpoint.admin.invitation :as invitation]))

(defn invitation []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (let [route (subscribe [:route])]
        (dispatch [:init-except-route])
        (invitation/check @route)))
    :reagent-render
    (fn []
      (let [form (subscribe [:form])
            error (subscribe [:error])]
        [:div
         [:h3 "accept invitation"]
         [:p.error (:error @error)]
         [:form.ui.form
          [:p.inline.field [:label "username" [:input {:type "text"
                                                       :placeholder "username"
                                                       :value (:username @form)
                                                       :on-change #(dispatch [:invitation/form.username (-> % .-target .-value)])}]]]
          [:p.inline.field [:label "password" [:input {:type "password"
                                                       :placeholder "password"
                                                       :value (:password @form)
                                                       :on-change #(dispatch [:invitation/form.password (-> % .-target .-value)])}]]]
          [:button.ui.teal.button {:type "submit" :on-click #(do (.preventDefault %))} "accept invitation"]]]))}))
