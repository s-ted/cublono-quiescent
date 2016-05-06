(defproject cubane/cublono-quiescent "0.1.0-SNAPSHOT"
  :description      "Lisp style templating to React Quiescent component"
  :url              "http://github.com/s-ted/cublono-quiescent"
  :author           "Sylvain Tedoldi"
  :min-lein-version "2.0.0"
  :license          {:name "Eclipse Public License"
                     :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies     [[org.clojure/clojure "1.8.0" :scope "provided"]
                     [org.clojure/clojurescript "1.7.228" :scope "provided"]

                     [cljsjs/react-with-addons "0.14.3-0" :scope "provided"]
                     [cljsjs/react-dom "0.14.3-1" :exclusions [cljsjs/react] :scope "provided"]
                     [cljsjs/react-dom-server "0.14.3-0" :exclusions [cljsjs/react] :scope "provided"]

                     [quiescent/quiescent "0.3.1" :scope "provided"]
                     [cubane/cublono "0.1.0-SNAPSHOT"]]

  :aliases          {"ci" ["do"
                           ["clean"]
                           ["test" ":default"]
                           ["doo" "phantom" "none" "once"]
                           ["doo" "phantom" "advanced" "once"]]}
  :clean-targets    ^{:protect false} [:target-path]
  :doo              {:build "test"}
  :cljsbuild        {:builds
                     {:test
                      {:compiler
                       {:asset-path "target/public/test"
                        :main cublono-quiescent.test-runner
                        :output-to "target/public/cublono-quiescent.js"
                        :output-dir "target/public/test"
                        :optimizations :none
                        :pretty-print true
                        :source-map true
                        :verbose true}
                       :source-paths ["src" "test"]}

                      :none
                      {:compiler
                       {:asset-path "target/none/out"
                        :main cublono-quiescent.test-runner
                        :output-to "target/none/cublono-quiescent.js"
                        :output-dir "target/none/out"
                        :optimizations :none
                        :pretty-print true
                        :source-map true
                        :verbose true}
                       :source-paths ["src" "test"]}

                      :advanced
                      {:compiler
                       {:asset-path "target/advanced/out"
                        :main cublono-quiescent.test-runner
                        :output-to "target/advanced/cublono-quiescent.js"
                        :optimizations :advanced
                        :pretty-print true
                        :verbose true}
                       :source-paths ["src" "test"]}}}
  :profiles           {:dev {:plugins [[lein-cljsbuild "1.1.2"]
                                       [lein-doo "0.1.6"]]
                             :dependencies [[hickory "0.6.0"]
                                            [cljsjs/react-bootstrap "0.28.1-1" :exclusions  [org.webjars.bower/jquery]]]}})
