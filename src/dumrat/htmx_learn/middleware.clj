(ns dumrat.htmx-learn.middleware
  (:require [ring.middleware.resource :refer [wrap-resource]]))

;; middleware and reitit wrappers

(defn wrap-tap-request-response [handler]
  (fn [request]
    (tap> request)
    (let [response (handler request)]
      (tap> response)
      response)))

(def tap-request-response-reitit-middleware
  {:name ::tap-request-response
   :wrap #'wrap-tap-request-response})

(def resource-middleware
  {:name ::resource-middleware
   :wrap (fn [handler] (wrap-resource handler "public"))})
