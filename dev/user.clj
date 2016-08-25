(ns user
  (:require [alembic.still :as a]
            [buddy.core.hash :as hash]
            [buddy.core.codecs :refer [bytes->hex]]
            [micropress.core :refer :all]
            [ragtime.jdbc :as jdbc]
            [ragtime.repl :refer :all]))

(defn reload-deps []
  (a/load-project))

(def config {:datastore (jdbc/sql-database {:connection-uri "jdbc:mysql://localhost:3306/micropress?user=micropress&password=p@ssw0rd&autoReconnect=true&useSSL=false"})
             :migrations (jdbc/load-resources "migrations")})

(defn hash-pwd
  "平文パスワードをSHA-256ハッシュ化して返却する."
  [row-pwd]
  (-> (hash/sha256 row-pwd)
      (bytes->hex)))
