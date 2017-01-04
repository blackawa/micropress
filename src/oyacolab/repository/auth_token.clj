(ns oyacolab.repository.auth-token
  (:require [yesql.core :refer [defqueries]]))

(defqueries "oyacolab/sql/auth_token.sql")
