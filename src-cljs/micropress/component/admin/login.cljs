(ns micropress.component.admin.login
  (:require [accountant.core :as accountant]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [micropress.endpoint.admin.authentication :refer [authenticate!]]
            [micropress.endpoint.admin.auth-token :as auth-token]))

(defn login []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (dispatch [:init-except-route])
      (auth-token/check :success-handler (fn [_] (accountant/navigate! "/admin/articles"))))
    :reagent-render
    (fn []
      (let [form (subscribe [:form])
            error (subscribe [:error])]
        [:div
         [:h3 "login"]
         [:p.error (:error @error)]
         [:form.ui.form
          [:p.inline.field [:label "username" [:input {:type "text"
                                                       :placeholder "username"
                                                       :value (:username @form)
                                                       :on-change #(dispatch [:login/form.username (-> % .-target .-value)])}]]]
          [:p.inline.field [:label "password" [:input {:type "password"
                                                       :placeholder "password"
                                                       :value (:password @form)
                                                       :on-change #(dispatch [:login/form.password (-> % .-target .-value)])}]]]
          [:button.ui.teal.button {:type "submit" :on-click #(do (.preventDefault %) (authenticate! @form))} "Login"]]]))}))
