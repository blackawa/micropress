(ns micropress.repository.article
  (:require [yesql.core :refer [defqueries]]))

(defqueries "micropress/sql/article.sql")
