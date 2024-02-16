(ns dumrat.htmx-learn.pages.main
  (:require [dumrat.htmx-learn.pages.util :refer [name->path] :as util]))

(defn- main-example-list-page [request]
  (util/wrap-page-hiccup
   request
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
      [:td [:span "Demonstrates bulk updating of multiple rows of data"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example3/main)} "Click To Load"]]
      [:td [:span "Demonstrates clicking to load more rows in a table"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example4/main)} "Delete Row"]]
      [:td [:span "Demonstrates row deletion in a table"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example5/main)} "Edit Row"]]
      [:td [:span "Demonstrates how to edit rows in a table"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example6/main)} "Lazy Loading"]]
      [:td [:span "Demonstrates how to lazy load content"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example7/main)} "Inline Validation"]]
      [:td [:span "Demonstrates how to do inline field validation"]]]]]))

(def routes
  [["/index.html"
    {:get {:handler main-example-list-page}
     :name ::root}]])
