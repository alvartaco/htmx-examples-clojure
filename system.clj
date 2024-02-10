(ns dumrat.htmx-learn.system
  (:require [integrant.core :as ig]))

(def config
  {::start-time nil})

(defmethod ig/init-key ::start-time [_ _]
  (java.util.Date.))

(def ^:private system (atom nil))

(defn stop-system! []
  (when @system
    (ig/halt! @system)
    (reset! system nil)))

(defn start-system! []
  (when @system
    (stop-system!))
  (let [s (reset! system (ig/init config))
        _ (tap> s)]
    s))

(defn get-system []
  @system)

(comment

  (start-system!)
  (stop-system!)
  (get-system)

  ,,)
