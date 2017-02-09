(ns micropress.endpoint.admin.invitation
  (:require [accountant.core :as accountant]
            [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch]]
            [micropress.endpoint.api :refer [request]]
            [micropress.util.cookie :refer [get-cookie]]
            [micropress.util.url :as url]))

(defn save []
  (let [auth-token (get-cookie :token)]
    (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/admin/invitations")
             :post
             (fn [xhrio]
               (let [location (.getResponseHeader xhrio "location")]
                 (dispatch [:init-form])
                 (dispatch [:data location])))
             :error-handler
             (fn [e xhrio]
               (condp = (.getStatus xhrio)
                 401 (accountant/navigate! "/login")
                 404 (accountant/navigate! "/login")
                 (do (js/console.error "unexpected error")
                     (accountant/navigate! "/login"))))
             :headers {"Content-Type" "application/edn"
                       "Authorization" (str "Bearer " auth-token)})))

(defn check [token]
  (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/admin/invitations/" token)
           :get
           (fn [xhrio])
           :error-handler
           (fn [_ xhrio]
             (dispatch [:error (read-string (.getResponseText xhrio))]))
           :headers {"Content-Type" "application/edn"}))
