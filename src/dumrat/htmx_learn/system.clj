(ns dumrat.htmx-learn.system
  (:require [integrant.core :as ig]
            [dumrat.htmx-learn.server :as server]))

;; System config
;;------------------------------------------;;
(def config
  {::start-time nil
   :server/port 3000
   :server/join? false
   ::server {:port (ig/ref :server/port)
             :join? (ig/ref :server/join?)}})

(defmethod ig/init-key ::start-time [_ _]
  (java.util.Date.))
(defmethod ig/init-key ::server [_ {:keys [port join?]}]
  (server/start-server {:port port :join? join?}))
(defmethod ig/init-key :server/port [_ v] v)
(defmethod ig/init-key :server/join? [_ v] v)

(defmethod ig/halt-key! ::server [_ server]
  (server/stop-server server))

;; System start/stop
;;------------------------------------------;;
(def ^:private system (atom nil))

(defn stop-system! []
  (when @system
    (tap> [:stop-system! "system stop"])
    (ig/halt! @system)
    (reset! system nil)))

(defn start-system! []
  (stop-system!)
  (let [s (reset! system (ig/init config))
        _ (tap> [:start-system! "system startup"])
        _ (tap> s)]
    s))
;;------------------------------------------;;

(comment

  (start-system!)
  (stop-system!)

  ,,)
