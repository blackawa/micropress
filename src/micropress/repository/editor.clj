(ns micropress.repository.editor
  (:require [yesql.core :refer [defqueries]]))

(defqueries "micropress/sql/editor.sql")
