(ns dumrat.htmx-learn.server
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [reitit.ring.middleware.parameters :as parameters]
            ;;
            [dumrat.htmx-learn.pages.example1 :as example1]))

(def ^:private routes
  [["/htmx-examples"
    ["/index.html"
     {:get {:handler (fn [request]
                       {:status 200 :headers {} :body "pong"})}}]
    example1/routes]])


(defn- rr-reloading-handler
  "if dev-mode?, recreates the handler on each request.
  See source for reitit.ring/reloading-ring-handler"
  [dev-mode?]
  (let [f (fn []
            (rr/ring-handler
             (rr/router routes
                        {:data {:middleware [parameters/parameters-middleware]}})))]
    (if dev-mode? (rr/reloading-ring-handler f) (f))))

(def dev-handler
  (rr/ring-handler
   (rr/router
     routes
     {:data {:middleware [parameters/parameters-middleware]}})))

(defn start-server [dev-mode? server-opts]
  (jetty/run-jetty
   #_(rr-reloading-handler dev-mode?)
   (wrap-reload #'dev-handler)
   server-opts))

(defn stop-server [server]
  (.stop server))

(comment

  (require '[ring.mock.request :as req]))
