(ns dumrat.htmx-learn.server
  (:require [reitit.ring :as rr]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.reload :refer [wrap-reload]]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.coercion.malli :as malli]
            [reitit.ring.coercion :as rrc]
            [reitit.coercion :as coercion]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.multipart :as multipart]
            [reitit.dev.pretty :as pretty]
            ;;
            [dumrat.htmx-learn.middleware :as middleware]
            [dumrat.htmx-learn.pages.main :as main]
            [dumrat.htmx-learn.pages.example1 :as example1]
            [dumrat.htmx-learn.pages.example2 :as example2]
            [dumrat.htmx-learn.pages.example3 :as example3]
            [dumrat.htmx-learn.pages.example4 :as example4]
            [dumrat.htmx-learn.pages.example5 :as example5]
            [dumrat.htmx-learn.pages.example6 :as example6]
            [dumrat.htmx-learn.pages.example7 :as example7]
            [dumrat.htmx-learn.pages.example8 :as example8]
            [dumrat.htmx-learn.pages.example9 :as example9]
            [dumrat.htmx-learn.pages.example10 :as example10]
            [dumrat.htmx-learn.pages.example11 :as example11]
            [dumrat.htmx-learn.pages.example12 :as example12]
            [dumrat.htmx-learn.pages.example13 :as example13]
            [dumrat.htmx-learn.pages.example14 :as example14]
            [dumrat.htmx-learn.pages.example15 :as example15]
            [dumrat.htmx-learn.pages.example16 :as example16]
            [dumrat.htmx-learn.pages.example17 :as example17]
            [dumrat.htmx-learn.pages.example18 :as example18]
            [dumrat.htmx-learn.pages.example19 :as example19]
            [dumrat.htmx-learn.pages.example20 :as example20]))

(def ^:private routes
  [["/assets/*" (rr/create-resource-handler)]
   ["/htmx-examples"
    main/routes
    example1/routes
    example2/routes
    example3/routes
    example4/routes
    example5/routes
    example6/routes
    example7/routes
    example8/routes
    example9/routes
    example10/routes
    example11/routes
    example12/routes
    example13/routes
    example14/routes
    example15/routes
    example16/routes
    example17/routes
    example18/routes
    example19/routes
    example20/routes]])

;;TODO: Compile coercers before prod
(def handler
  (rr/ring-handler
   (rr/router
    routes
    {:data {;;:exception pretty/exception
            :middleware [middleware/tap-response-middleware
                         parameters/parameters-middleware
                         multipart/multipart-middleware
                         muuntaja/format-negotiate-middleware
                         muuntaja/format-response-middleware
                         muuntaja/format-request-middleware
                         rrc/coerce-exceptions-middleware
                         middleware/session-middleware
                         middleware/hiccup->html-middlware
                         rrc/coerce-request-middleware
                         rrc/coerce-response-middleware
                         middleware/tap-request-middleware
                         middleware/exception-middleware]
            :coercion (reitit.coercion.malli/create
                       {;; set of keys to include in error messages
                        :error-keys #{#_:type :coercion :in :schema :value :errors :humanized #_:transformed}
                           ;; strip-extra-keys (affects only predefined transformers)
                        :strip-extra-keys true
                           ;; add/set default values
                        :default-values true
                           ;; malli options
                        :options nil})
            :muuntaja m/instance}
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
    (reitit.core/match-by-name router ::example1/contact-view-page)))
