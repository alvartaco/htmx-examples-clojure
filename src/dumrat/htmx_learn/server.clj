(ns dumrat.htmx-learn.server
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [reitit.ring.middleware.parameters :as parameters]
            [dumrat.htmx-learn.middleware :as middleware]
            ;;
            [dumrat.htmx-learn.pages.main :as main]
            [dumrat.htmx-learn.pages.example1 :as example1]
            [dumrat.htmx-learn.pages.example2 :as example2]))

(def ^:private routes
  [["/assets/*" (rr/create-resource-handler)]
   ["/htmx-examples"
    main/routes
    example1/routes
    example2/routes]])

(def handler
  (rr/ring-handler
   (rr/router
    routes
    {:data {:middleware [parameters/parameters-middleware
                         middleware/wrap-tap-request-reponse
                         middleware/wrap-hiccup->html]}
     :conflicts (constantly nil)})))

(defn start-server [dev-mode? server-opts]
  (jetty/run-jetty
   (if dev-mode?
     (wrap-reload #'handler)
     handler)
   server-opts))

(defn stop-server [server]
  (.stop server))

(comment

  (require '[ring.mock.request :as req])
  (handler {:request-method :get
            :uri "/index.html"})

  (handler {:request-method :get
            :uri "/css/site.css"})

  (let [router (rr/router example1/routes)]
    (reitit.core/match-by-name router ::example1/contact-view-page))

  ,,)
