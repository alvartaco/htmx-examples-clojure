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
      [:td [:span "Demonstrates various animation techniques"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example13/main)} "File Upload"]]
      [:td [:span "Demonstrates how to upload a file via ajax with a progress bar"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example14/main)} "Preserving File Inputs after Form Errors"]]
      [:td [:span "Demonstrates how to preserve file inputs after form errors"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example15/main)} "Dialogs - Browser"]]
      [:td [:span "Demonstrates the prompt and confirm dialogs"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example16/main)} "Dialogs - UIKit"]]
      [:td [:span "Demonstrates modal dialogs using UIKit"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example17/main)} "Dialogs - Bootstrap"]]
      [:td [:span "Demonstrates modal dialogs using Bootstrap"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example18/main)} "Dialogs - Custom"]]
      [:td [:span "Demonstrates modal dialogs from scratch"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example19/main)} "Tabs (Using HATEOAS)"]]
      [:td [:span "Demonstrates how to display and select tabs using HATEOAS principles"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example20/main)} "Tabs (Using JavaScript)"]]
      [:td [:span "Demonstrates how to display and select tabs using JavaScript"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example21/main)} "Keyboard Shortcuts"]]
      [:td [:span "Demonstrates how to create keyboard shortcuts for htmx enabled elements"]]]
     [:tr
      [:td [:a {:href (name->path request :dumrat.htmx-learn.pages.example22/main)} "Drag & Drop / Sortable"]]
      [:td [:span "Demonstrates how to use htmx with the Sortable.js plugin to implement drag-and-drop reordering"]]]]]))

(def routes
  [["/index.html"
    {:get {:handler main-example-list-page}
     :name :root}]])
