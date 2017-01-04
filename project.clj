(defproject oyacolab "0.1.0-SNAPSHOT"
  :description "API based Contents Management System"
  :url "FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [com.stuartsierra/component "0.3.1"]
                 [compojure "1.5.1"]
                 [duct "0.8.2"]
                 [environ "1.1.0"]
                 [hanami "0.1.0"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.1"]
                 [ring-jetty-component "0.3.1"]
                 [ring-webjars "0.1.1"]
                 [org.slf4j/slf4j-nop "1.7.21"]
                 [org.webjars/normalize.css "3.0.2"]
                 [org.webjars/pure "0.6.0"]
                 [duct/hikaricp-component "0.1.0"]
                 [org.postgresql/postgresql "9.4.1211"]
                 [duct/ragtime-component "0.1.4"]
                 [liberator "0.13"]
                 [yesql "0.5.3"]
                 [clj-time "0.13.0"]
                 [hiccup "1.0.5"]
                 [buddy "1.2.0"]
                 [amazonica "0.3.80"]
                 ;; necessary to work amazonica fine
                 [com.fasterxml.jackson.core/jackson-databind "2.6.6"]
                 [re-frame "0.9.1"]
                 [secretary "1.2.3"]
                 [venantius/accountant "0.1.7"]
                 [markdown-clj "0.9.91"]]
  :plugins [[lein-environ "1.0.3"]
            [lein-cljsbuild "1.1.2"]]
  :main ^:skip-aot oyacolab.main
  :uberjar-name "oyacolab-standalone.jar"
  :target-path "target/%s/"
  :resource-paths ["resources" "target/cljsbuild"]
  :prep-tasks [["javac"] ["cljsbuild" "once"] ["compile"]]
  :cljsbuild
  {:builds
   [{:id "main"
     :jar true
     :source-paths ["src-cljs"]
     :compiler
     {:output-to     "target/cljsbuild/oyacolab/public/js/main.js"
      :optimizations :advanced
      :closure-defines {goog.DEBUG false}}}]}
  :aliases {"setup"  ["run" "-m" "duct.util.repl/setup"]
            "deploy" ["do"
                      ["vcs" "assert-committed"]
                      ["vcs" "push" "heroku" "master"]]}
  :repl-options {:port 51979}
  :profiles
  {:dev  [:project/dev  :profiles/dev]
   :test [:project/test :profiles/test]
   :repl {:resource-paths ^:replace ["resources" "dev/resources" "target/figwheel"]
          :prep-tasks     ^:replace [["javac"] ["compile"]]}
   :uberjar {:aot :all}
   :profiles/dev  {}
   :profiles/test {}
   :project/dev   {:dependencies [[duct/generate "0.8.2"]
                                  [reloaded.repl "0.2.3"]
                                  [org.clojure/tools.namespace "0.2.11"]
                                  [org.clojure/tools.nrepl "0.2.12"]
                                  [eftest "0.1.1"]
                                  [com.gearswithingears/shrubbery "0.4.1"]
                                  [binaryage/devtools "0.8.2"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [duct/figwheel-component "0.3.3"]
                                  [figwheel "0.5.8"]]
                   :source-paths   ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :repl-options {:init-ns user
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}
                   :env {:port "3000"}}
   :project/test  {}})
