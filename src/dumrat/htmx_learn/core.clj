(ns dumrat.htmx-learn.core
  (:require [dumrat.htmx-learn.system :as system]))

(defn start! []
  (system/start-system!))

(comment

  (start!)
  (system/get-system)

  ,,)
