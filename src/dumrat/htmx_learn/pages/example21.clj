(ns dumrat.htmx-learn.pages.example21
  (:require [dumrat.htmx-learn.pages.util :as util]
            [reitit.core :as rc]))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:button
    {:hx-trigger "click, keyup[altKey&&shiftKey&&key=='D'] from:body",
     :hx-post (util/name->path request ::doit)}
    "Do It! (alt-shift-D)"]))

(defn- doit [request]
  (util/hiccup-response
   [:button
    {:hx-trigger "click, keyup[altKey&&shiftKey&&key=='D'] from:body",
     :hx-post (util/name->path request ::doit)}
    "Did It! (alt-shift-D)"]))

(def routes
  ["/example21"
   ["" {:get {:handler main}
        :name ::main}]
   ["/doit" {:post {:handler doit}
             :name ::doit}]])
