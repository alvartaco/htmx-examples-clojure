(ns dumrat.htmx-learn.pages.example18
  (:require [dumrat.htmx-learn.pages.util :as util]))

(defn- main [request]
  (util/wrap-page-hiccup
    request
    [:button
     {:hx-get (util/name->path request ::modal) :hx-target "body", :hx-swap "beforeend"}
     "Open a Modal"]))

(defn- modal [request]
  (util/hiccup-response
   [:div
    {:id "modal",
     :_
     "on closeModal add .closing then wait for animationend then remove me"}
    [:div {:class "modal-underlay", :_ "on click trigger closeModal"}]
    [:div
     {:class "modal-content"}
     [:h1 "Modal Dialog"]
     "This is the modal content.\n\t\tYou can put anything here, like text, or a form, or an image."
     [:br]
     [:br]
     [:button {:_ "on click trigger closeModal"} "Close"]]]))

(def routes
  ["/example18"
   [""
    {:get {:handler main}
     :name ::main}]
   ["/modal" {:get {:handler modal}
              :name ::modal}]])

(comment
  #_f)
