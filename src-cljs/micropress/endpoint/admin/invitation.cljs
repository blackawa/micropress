(ns micropress.endpoint.admin.invitation
  (:require [accountant.core :as accountant]
            [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch]]
            [micropress.endpoint.api :refer [request]]
            [micropress.endpoint.admin.authentication :as authentication]
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

(defn check [route]
  (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/invitations/" (second route))
           :get
           (fn [xhrio])
           :error-handler
           (fn [_ xhrio]
             (dispatch [:route [:not-found]]))
           :headers {"Content-Type" "application/edn"}))

(defn accept [route form]
  (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/invitations/" (second route))
           :post
           (fn [xhrio]
             (authentication/authenticate! (str form)))
           :error-handler
           (fn [_ xhrio]
             (condp = (.getStatus xhrio)
               404 (dispatch [:route [:not-found]])
               422 (dispatch [:error (read-string (.getResponseText xhrio))])
               (do (js/console.error "unexpected error")
                   (accountant/navigate! "/login"))))
           :headers {"Content-Type" "application/edn"}
           :body (str form)))
