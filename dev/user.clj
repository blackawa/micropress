(ns user
  (:require [alembic.still :as a]
            [micropress.core :refer :all]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :refer :all]))

(defn reload-deps []
  (a/load-project))

(def config {:datastore (jdbc/sql-database {:connection-uri "jdbc:mysql://localhost:3306/micropress?user=micropress&password=p@ssw0rd"})
             :migrations (jdbc/load-resources "migrations")})
