(ns oyacolab.component.admin.login
  (:require [accountant.core :as accountant]
            [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]
            [oyacolab.endpoint.admin.authentication :refer [authenticate!]]
            [oyacolab.endpoint.admin.auth-token :as auth-token]))

(defn login []
  (reagent/create-class
   {:component-will-mount
    (fn []
      (auth-token/check :success-handler (fn [_] (accountant/navigate! "/admin/articles")))
      (dispatch [:init-login-db]))
    :reagent-render
    (fn []
      (let [form (subscribe [:login.form])
            error (subscribe [:error])]
        [:div
         [:h3 "login"]
         [:p.error (:error @error)]
         [:form
          [:p [:label "username" [:input {:type "text"
                                    :placeholder "username"
                                    :value (:username @form)
                                    :on-change #(dispatch [:login.form.username (-> % .-target .-value)])}]]]
          [:p [:label "password" [:input {:type "password"
                                    :placeholder "password"
                                    :value (:password @form)
                                    :on-change #(dispatch [:login.form.password (-> % .-target .-value)])}]]]
          [:button {:type "submit" :on-click #(do (.preventDefault %) (authenticate! @form))} "Login"]]]))}))
