(ns micropress.service.authorization
  (:require [micropress.repository.session :as session]))

(defn find-by-token
  [token]
  (:authorities (session/find-by-token-with-user token)))
