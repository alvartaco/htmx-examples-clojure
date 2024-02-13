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

(defn hiccup->html [content]
  (str (html content)))

;; TODO: 1. Remove hard-coded back-link by adding request to params.
;; TODO: 2. Hide back-link on main page.
(defn page [body]
  [:html
   (header)
   [:body {:hx-boost true}
    body
    [:section {:style {:margin-top "10px"}}]
    [:a {:href "/htmx-examples/index.html"} "Back to main page"]]])

(defn name->path [{::r/keys [router]} name]
  (-> router
      (match-by-name name)
      (match->path)))

(defn hiccup-response [body]
  (-> body
      resp/response
      (resp/header "Content-Type" "hiccup")))
