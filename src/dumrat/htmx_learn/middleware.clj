(ns dumrat.htmx-learn.middleware
  (:require [hiccup2.core :refer [html]]))

;; middleware and reitit wrappers

(defn- tap-request-response [handler]
  (fn [request]
    (tap> request)
    (let [response (handler request)]
      (tap> response)
      #_(tap> (with-meta (:body response) {:portal.viewer/default :portal.viewer/hiccup}))
      response)))

(def wrap-tap-request-reponse
  {:name ::tap-request-response
   :wrap #'tap-request-response})

(defn- hiccup->html [handler]
  (fn [request]
    (let [response (handler request)]
      (if-not (= "hiccup" (get-in response [:headers "Content-Type"]))
        response
        (update response :body (comp str (fn [x] (html x))))))))

(def wrap-hiccup->html
  {:name ::tap-request-response
   :wrap #'hiccup->html})
