(ns dumrat.htmx-learn.pages.util
  (:require [hiccup2.core :refer [html]]
            [reitit.core :refer [match-by-name match->path] :as r]))

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

(defn page [body]
  [:html
   (header)
   [:body {:hx-boost true} body]])

(defn name->path [{::r/keys [router]} name]
  (-> router
      (match-by-name name)
      (match->path)))
