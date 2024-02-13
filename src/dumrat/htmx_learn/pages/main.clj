(ns dumrat.htmx-learn.pages.main
  (:require [dumrat.htmx-learn.pages.util :refer [name->path page hiccup-response]]))


(defn- main-example-list-page [request]
  (hiccup-response
   (page
    [:table
     [:thead
      [:tr
       [:th "Pattern"] [:th "Description"]]]
     [:tbody
      [:tr
       [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example1/main)} "Click To Edit"]]
       [:td [:span "Demonstrates inline editing of a data object"]]]
      [:tr
       [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example2/main)} "Bulk Update"]]
       [:td [:span "Demonstrates bulk updating of multiple rows of data"]]]]])))

(def routes
  [["/index.html"
    {:get {:handler main-example-list-page}
     :name ::root}]])
