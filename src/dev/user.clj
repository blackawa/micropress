(ns user
  (:require [buddy.core.codecs :refer [bytes->hex]]
            [buddy.core.hash :as hash]
            [environ.core :refer [env]]
            [micropress.core :refer :all]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :as ragtime]))

(def config {:datastore (jdbc/sql-database
                         {:connection-uri (format "jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&useSSL=false"
                                                  (:host env)
                                                  (:port env)
                                                  (:db env)
                                                  (:username env)
                                                  (:password env))})
             :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (ragtime/migrate config))

(defn rollback []
  (ragtime/rollback config))
