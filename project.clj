(defproject micropress "0.1.0-SNAPSHOT"
  :description "contents management system with microservice architecture"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.6.1"

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.89"]
                 [org.clojure/core.async "0.2.385"
                  :exclusions [org.clojure/tools.reader]]
                 [cljsjs/react "15.2.1-1"]
                 [cljsjs/react-dom "15.2.1-1"]
                 [sablono "0.7.3"]
                 [org.omcljs/om "1.0.0-alpha40"]
                 [ring "1.5.0"]
                 [compojure "1.5.1"]]

  :plugins [[lein-figwheel "0.5.4-7"]
            [lein-cljsbuild "1.1.3" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src" "src/main/clj"]
  :test-paths ["test" "src/test/clj"]
  :resource-paths ["src/main/resources"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/main/cljs"]
                :figwheel {:on-jsload "micropress.core/on-js-reload"}

                :compiler {:main micropress.core
                           :asset-path "js/compiled/out"
                           :output-to "src/main/resources/public/js/compiled/micropress.js"
                           :output-dir "src/main/resources/public/js/compiled/out"
                           :source-map-timestamp true
                           :preloads [devtools.preload]}}
               {:id "min"
                :source-paths ["src/main/cljs"]
                :compiler {:output-to "src/main/resources/public/js/compiled/micropress.js"
                           :main micropress.core
                           :optimizations :advanced
                           :pretty-print false}}]}

  :figwheel {:css-dirs ["src/main/resources/public/css"]}

  :profiles {:dev {:dependencies [[binaryage/devtools "0.7.2"]
                                  [figwheel-sidecar "0.5.4-7"]
                                  [com.cemerick/piggieback "0.2.1"]
                                  [alembic "0.3.2"]]

                   :source-paths ["src" "src/main/clj" "dev"]
                   :plugins [[cider/cider-nrepl "0.13.0"]]
                   :repl-options {:init (set! *print-length* 50)
                                  :nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})
