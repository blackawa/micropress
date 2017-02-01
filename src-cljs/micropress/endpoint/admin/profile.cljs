(ns micropress.endpoint.admin.profile
  (:require [accountant.core :as accountant]
            [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch]]
            [micropress.endpoint.api :refer [request]]
            [micropress.util.cookie :refer [get-cookie]]
            [micropress.util.url :as url]))

(defn fetch-by-token []
  (let [auth-token (get-cookie :token)]
    (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/admin/profile")
             :get
             (fn [xhrio]
               (dispatch [:data (read-string (.getResponseText xhrio))]))
             :error-handler
             (fn [e xhrio]
               (condp = (.getStatus xhrio)
                 401 (accountant/navigate! "/login")
                 404 (accountant/navigate! "/login")
                 (do (js/console.err "unexpected error")
                     (accountant/navigate! "/login"))))
             :headers {"Content-Type" "application/edn"
                       "Authorization" (str "Bearer " auth-token)})))
