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
      [:td [:span "Demonstrates how to do inline field validation"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example8/main)} "Infinite Scroll"]]
      [:td [:span "Demonstrates infinite scrolling of a page"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example9/main)} "Active Search"]]
      [:td [:span "Demonstrates the active search box pattern"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example10/main)} "Progress Bar"]]
      [:td [:span "Demonstrates a job-runner like progress bar"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example11/main)} "Value Select"]]
      [:td [:span "Demonstrates making the values of a select dependent on another select"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example12/main)} "Animations"]]
      [:td [:span "Demonstrates various animation techniques"]]]]]))

(def routes
  [["/index.html"
    {:get {:handler main-example-list-page}
     :name :root}]])
