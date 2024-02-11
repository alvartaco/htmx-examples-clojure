(ns dumrat.htmx-learn.pages.example1
  (:require [hiccup2.core :as hiccup]))

(defn contact-view-page [request]
  {:status  200
   :headers {}
   :body
   (str
    (hiccup/html
     [:html
      [:head
       [:meta {:charset "utf-8"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
       [:meta {:name "description" :content "This is an implementation for htmx official example 1 - Click to Edit with a Clojure back-end."}]
       [:title "</> htmx ~ Examples ~ Click to Edit"]
       [:link {:rel "stylesheet" :href "[TODO]"}]
       [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]]
      [:body
       [:div {:hx-target "this" :hx-swap "outerHTML"}
        [:div [:label "First Name"] ": Joe"]
        [:div [:label "Last Name"] ": Blow"]
        [:div [:label "Email"] ": joe@blow.com"]
        [:button {:hx-get "/htmx-examples/example1/contact/1/edit-form"} "Click to Edit"]]]]))})

(defn contact-edit-form [request]
  {:status 200
   :headers {}
   :body
   (str (hiccup/html
         [:form {:hx-put "htmx-examples/example1/contact/1", :hx-target "this", :hx-swap "outerHTML"}
          [:div
           [:label "First Name"]
           [:input {:type "text", :name "firstName", :value "Joe"}]]
          [:div {:class "form-group"}
           [:label "Last Name"]
           [:input {:type "text", :name "lastName", :value "Blow"}]]
          [:div {:class "form-group"}
           [:label "Email Address"]
           [:input {:type "email", :name "email", :value "joe@blow.com"}]]
          [:button {:class "btn"} "Submit"]
          [:button {:class "btn", :hx-get "/htmx-examples/example1/contact/1"} "Cancel"]]))})

(def routes
  ["/example1"
   ["/contact/1"
    {:get {:handler contact-view-page}}]
   ["/contact/1/edit-form"
    {:get {:handler contact-edit-form}}]])
