(ns dumrat.htmx-learn.server
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as jetty]
            [reitit.ring.middleware.parameters :as parameters]))


(def ^:private dev-routes
  [["/ping" {:get {:handler (fn [request]
                              {:status 200 :headers {} :body "pong"})}}]])

(defn- handler
  "if dev-mode?, recreates the handler on each request.
  See source for reitit.ring/reloading-ring-handler"
  [dev-mode?]
  (let [f (fn []
            (rr/ring-handler
             (rr/router dev-routes
                        {:data {:middleware [parameters/parameters-middleware]}})))]
    (if dev-mode? (rr/reloading-ring-handler f) (f))))

(defn start-server [dev-mode? server-opts]
  (jetty/run-jetty (handler dev-mode?) server-opts))

(defn stop-server [server]
  (.stop server))

(comment

  (require '[ring.mock.request :as req])

  ,,)
