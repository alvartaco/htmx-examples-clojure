(ns dumrat.htmx-learn.middleware)

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
