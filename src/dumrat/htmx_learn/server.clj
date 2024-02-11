(ns dumrat.htmx-learn.server
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as jetty]))


(def ^:private dev-routes
  [["/ping" {:get {:handler (fn [request]
                              {:status 200 :headers {} :body "pong"})}}]])

(defn handler [dev-mode?]
  (let [f (fn []
            (rr/ring-handler
             (rr/router dev-routes)))]
    (if dev-mode? (rr/reloading-ring-handler f) (f))))

(defn start-server [opts]
  (jetty/run-jetty (handler true) opts))

(defn stop-server [server]
  (.stop server))

(comment

  (require '[ring.mock.request :as req])

  ,,)
