(ns dumrat.htmx-learn.pages.example15
  (:require [dumrat.htmx-learn.pages.util :as util]
            [hiccup2.core :as h]))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    [:button {:class "btn"
              :hx-post (util/name->path request ::submit)
              :hx-prompt "Enter a string"
              :hx-confirm "Are you sure?"
              :hx-target "#response"}
     "Prompt Submission"]
    [:div {:id "response"}]]))

(defn- submit [request]
  (let [prompt (get-in request [:headers "hx-prompt"])]
    (util/hiccup-response
     (h/raw (str "User entered : <i>" prompt "</i>")))))

(def routes
  ["/example15"
   [""
    {:get {:handler main}
     :name ::main}]
   ["/submit"
    {:post {:handler submit}
     :name ::submit}]])

(comment
  #_f)
