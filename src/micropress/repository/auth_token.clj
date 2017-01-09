(ns micropress.repository.auth-token
  (:require [yesql.core :refer [defqueries]]))

(defqueries "micropress/sql/auth_token.sql")
