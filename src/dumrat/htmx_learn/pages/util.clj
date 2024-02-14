(ns dumrat.htmx-learn.pages.util
  (:require [hiccup2.core :refer [html]]
            [reitit.core :refer [match-by-name match->path] :as r]
            [ring.util.response :as resp]))

(defn header []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:meta {:name "description" :content "This is an implementation for htmx official example 1 - Click to Edit with a Clojure back-end."}]
   [:title "</> htmx ~ Examples ~ Click to Edit"]
   [:link {:rel "stylesheet" :href "/assets/css/site.css"}]
   [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]])

;; TODO: 1. Remove hard-coded back-link by adding request to params.
;; TODO: 2. Hide back-link on main page.
(defn wrap-page
  "If request doesn't have htmx headers, wrap in page. Otherwise returns as-is"
  [request body]
  (if-not (get-in request [:headers "hx-request"])
    [:html
     (header)
     [:body {:hx-boost true}
      body
      [:section {:style {:margin-top "10px"}}]
      [:a {:href "/htmx-examples/index.html"} "Back to main page"]]]
    body))

(defn name->path [{::r/keys [router]} name]
  (-> router
      (match-by-name name)
      (match->path)))

(defn hiccup-response [body]
  (-> body
      resp/response
      (resp/header "Content-Type" "hiccup")))

(defn get-state-or-init [path init-val]
  (fn [request]
    (let [state (get request :session)]
      (when-not (get @state path)
        (swap! state assoc path (atom init-val)))
      (get @state path))))
