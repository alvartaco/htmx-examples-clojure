(ns dumrat.htmx-learn.pages.example2
  (:require [dumrat.htmx-learn.pages.util :refer [page name->path hiccup-response]]))

(defn- example2 [request]
  (hiccup-response
   (page
    [:form {:id "checked-contacts"}
     [:table
      [:thead
       [:tr
        [:th "Name"]
        [:th "Email"]
        [:th "Active"]]]
      [:tbody {:id "tbody"}
       [:tr
        [:td "Joe Smith"]
        [:td "joe@smith.org"]
        [:td
         [:input {:type "checkbox", :name "active:joe@smith.org"}]]]]]
     [:input {:type "submit", :value "Bulk Update"}]
     [:span {:id "toast"}]])))

(def routes
  ["/example2"
   ["" {:get {:handler example2}
        :name ::main}]])
