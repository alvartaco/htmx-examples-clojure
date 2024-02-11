(ns dumrat.htmx-learn.core
  (:require [dumrat.htmx-learn.system :as system])
  (:gen-class))

(defn start! []
  (system/start-system! false))

(defn -main [& args]
  (start!))
