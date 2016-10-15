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
                 ;; database connection
                 [org.clojure/java.jdbc "0.6.1"]
                 [mysql/mysql-connector-java "6.0.3"]
                 [korma "0.4.3"]
                 [ragtime "0.6.0"]
                 ;; logging
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-api "1.7.0"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 ;; other libraries
                 [buddy "1.0.0"]
                 [environ "1.1.0"]
                 [clj-time "0.12.0"]
                 [prismatic/schema "1.1.3"]]
  :plugins [[lein-environ "1.1.0"]]
  :source-paths ["src/main/clj"]
  :test-paths ["src/test/clj"]
  :resource-paths ["src/main/resources"]
  :clean-targets ^{:protect false} ["target"]

  :repl-options {:port 51423}

  :profiles {:dev {:dependencies [[alembic "0.3.2"]]
                   :source-paths ["src/main/clj" "src/dev"]
                   :repl-options {:init (set! *print-length* 50)}
                   :env {:host "127.0.0.1"
                         :port "3306"
                         :db "micropress"
                         :username "micropress"
                         :password "p@ssw0rd"}}
             :test {:env {:host "127.0.0.1"
                          :port "3306"
                          :db "micropress_test"
                          :username "micropress"
                          :password "p@ssw0rd"}}})
