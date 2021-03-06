(ns micropress.endpoint.admin.auth-token
  (:require [accountant.core :as accountant]
            [cljs.reader :refer [read-string]]
            [re-frame.core :refer [dispatch]]
            [micropress.endpoint.api :refer [request]]
            [micropress.util.cookie :refer [get-cookie]]))

(defn check [& {:keys [success-handler]}]
  (if-let [auth-token (get-cookie :token)]
    (request (str (.. js/location -procotol) "//" (.. js/location -host) "/api/admin/auth-token")
             :get
             (if success-handler success-handler #())
             :error-handler
             (fn [e xhrio]
               (when (= 401 (.getStatus xhrio))
                 (accountant/navigate! "/login")))
             :headers {"Content-Type" "application/edn"
                       "Authorization" (str "Bearer " auth-token)})
    (accountant/navigate! "/login")))
