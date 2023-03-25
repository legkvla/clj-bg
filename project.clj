(defproject clj-bg "0.1.0-SNAPSHOT"
  :description "Library to organize work with periodical bg jobs"
  :url "http://example.com/FIXME"
  :dependencies
  [
    [org.clojure/clojure "1.10.3"]

    [metrics-clojure "2.10.0"]

    [org.clojure/tools.logging "1.1.0"]]

  :repl-options {:init-ns clj-bg.worker})
