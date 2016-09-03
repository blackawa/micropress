(defproject micropress "0.1.0-SNAPSHOT"
  :description "Api server for contents management system with microservice architecture"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.6.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 ;; web application
                 [ring "1.5.0"]
                 [fogus/ring-edn "0.3.0"]
                 [compojure "1.5.1"]
                 ;; databse connection
                 [org.clojure/java.jdbc "0.6.1"]
                 [mysql/mysql-connector-java "6.0.3"]
                 [korma "0.4.3"]
                 [ragtime "0.6.0"]
                 ;; other libraries
                 [buddy "1.0.0"]
                 [clj-time "0.12.0"]]

  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  :resource-paths ["src/main/resources"]

  :clean-targets ^{:protect false} ["target"]

  :profiles {:dev {:dependencies [[alembic "0.3.2"]]
                   :source-paths ["src/main/clj" "dev"]
                   :repl-options {:init (set! *print-length* 50)}}})
