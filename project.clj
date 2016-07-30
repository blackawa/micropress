(defproject micropress "0.1.0-SNAPSHOT"
  :description "Api server for contents management system with microservice architecture"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.6.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.5.0"]
                 [compojure "1.5.1"]
                 [mysql/mysql-connector-java "6.0.3"]
                 [org.clojure/java.jdbc "0.6.2-alpha2"]]

  :source-paths ["src" "src/main/clj"]
  :test-paths ["test" "src/test/clj"]
  :resource-paths ["src/main/resources"]

  :clean-targets ^{:protect false} ["target"]

  :profiles {:dev {:dependencies [[alembic "0.3.2"]]
                   :source-paths ["src" "src/main/clj" "dev"]
                   :repl-options {:init (set! *print-length* 50)}}})
