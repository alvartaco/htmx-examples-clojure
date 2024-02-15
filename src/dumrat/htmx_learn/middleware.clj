(ns dumrat.htmx-learn.middleware
  (:require [hiccup2.core :refer [html]]
            [dumrat.htmx-learn.session :as session]))

;; middleware and reitit wrappers

(defn- tap-request-response [handler]
  (fn [request]
    (tap> request)
    (let [response (handler request)]
      (tap> response)
      response)))

(def wrap-tap-request-response
  {:name ::tap-request-response
   :wrap #'tap-request-response})

(defn- hiccup->html
  "If content-Type is hiccup, convert body to html string"
  [handler]
  (fn [request]
    (let [response (handler request)]
      (if-not (= "hiccup" (get-in response [:headers "Content-Type"]))
        response
        (-> response
            (update :body (comp str (fn [x] (html x))))
            (assoc-in [:headers "Content-Type"] "text/html"))))))

(def wrap-hiccup->html
  {:name ::tap-request-response
   :wrap #'hiccup->html})

(defn- session-middleware
  "Basic session middleware to give each client a unique session.
   If session isn't present, creates new server side session and sets id as cookie in response
   Injects session into request."
  [handler]
  (fn [request]
    (let [cookie (get-in request [:headers "cookie"])
          [_ request-session-id] (re-matches #"sessionId=(.*)" (or cookie ""))
          [session-id session] (session/get-or-create-session request-session-id)
          response (handler (assoc request :session session))]
      (if (not= session-id request-session-id)
        (assoc-in response [:headers "Set-Cookie"] (str "sessionId=" session-id ";path=/"))
        response))))

(def wrap-session
  {:name ::session
   :wrap #'session-middleware})

(comment

  (re-matches #"sessionId=(.*)" "sessionId=what")

  ,,)
