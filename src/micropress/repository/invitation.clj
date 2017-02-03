(ns micropress.repository.invitation
  (:require [yesql.core :refer [defqueries]]))

(defqueries "micropress/sql/invitation.sql")
