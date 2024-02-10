(ns dumrat.htmx-learn.system
  (:require [integrant.core :as ig]))

(def config
  {::start-time nil})

(defmethod ig/init-key ::start-time [_ _]
  (java.util.Date.))

(def ^:private system (atom nil))

(defn start-system []
  (when @system
    (stop-system!))
  (reset! system (ig/init config)))

(defn stop-system! []
  (when @system
    (ig/halt! @system)
    (reset! system nil)))

(defn get-system []
  @system)

(comment

  (start-system)
  (stop-system!)
  (get-system)

  ,,)
