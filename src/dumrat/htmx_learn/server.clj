(ns dumrat.htmx-learn.server
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [reitit.ring.middleware.parameters :as parameters]
            [dumrat.htmx-learn.middleware :as middleware]
            ;;
            [dumrat.htmx-learn.pages.example1 :as example1]))

(def ^:private routes
  [["/htmx-examples"
    ["/index.html"
     {:get {:handler (fn [request]
                       {:status 200 :headers {} :body "pong"})}}]
    example1/routes]])

(def handler
  (rr/ring-handler
   (rr/router
     routes
     {:data {:middleware [parameters/parameters-middleware
                          middleware/tap-request-response-reitit-middleware]}})))

(defn start-server [dev-mode? server-opts]
  (jetty/run-jetty
   (if dev-mode?
     (wrap-reload #'handler)
     handler)
   server-opts))

(defn stop-server [server]
  (.stop server))

(comment

  (require '[ring.mock.request :as req]))
