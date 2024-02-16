(ns dumrat.htmx-learn.middleware
  (:require [hiccup2.core :refer [html]]
            [dumrat.htmx-learn.session :as session]
            [reitit.ring.middleware.exception :as exception]))

;; middleware and reitit wrappers
(defn- tap-request [handler]
  (fn [request]
    (tap> {:request request})
    (handler request)))

(def tap-request-middleware
  {:name ::tap-request
   :wrap #'tap-request})

(defn- tap-response [handler]
  (fn [request]
    (let [response (handler request)]
      (tap> {:response response})
      response)))

(def tap-response-middleware
  {:name ::tap-response
   :wrap #'tap-response})

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

(def hiccup->html-middlware
  {:name ::hiccup->html-middleware
   :wrap #'hiccup->html})

(defn- add-session
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

(def session-middleware
  {:name ::session-middleware
   :wrap #'add-session})

;; Below exception middleware logic is from here: https://github.com/metosin/reitit/blob/master/doc/ring/exceptions.md

;; type hierarchy
(derive ::error ::exception)
(derive ::failure ::exception)
(derive ::horror ::exception)

(defn handler [message exception request]
  (tap> exception)
  {:status 500
   :body {:message message
          :exception (.getClass exception)
          :data (ex-data exception)
          :uri (:uri request)}})

(def exception-middleware
  (exception/create-exception-middleware
    (merge
      exception/default-handlers
      {;; ex-data with :type ::error
       ::error (partial handler "error")

       ;; ex-data with ::exception or ::failure
       ::exception (partial handler "exception")

       ;; override the default handler
       ::exception/default (partial handler "default")

       ;; print stack-traces for all exceptions
       ::exception/wrap (fn [handler e request]
                          (println "ERROR" (pr-str (:uri request)))
                          (handler e request))})))

;; TODO: Remove the functional third party middleware and rewrite simpler versions that are needed for this project.
;; TODO: exception handling, param coercion and keyword params can be written trivially I guess.

(comment

  (re-matches #"sessionId=(.*)" "sessionId=what")

  ,,)
