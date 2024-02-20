(ns dumrat.htmx-learn.pages.example22
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]))

(defn- gen-draggable-items [numlist]
  (list
   [:div {:class "htmx-indicator"} "Updating..."]
   (map
    (fn [i]
      [:div
       {:style
        "border:1px solid #DEDEDE; padding:12px; margin: 8px; width:200px; cursor: grab",
        :ondrag "this.style.cursor = 'grabbing'",
        :draggable "false",
        :class ""}
       [:input {:type "hidden", :name "item", :value i}]
       (str "Item " i)])
    numlist)))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    [:form
     {:class "sortable", :hx-post (util/name->path request ::items) :hx-trigger "end"
      :hx-swap "innerHTML"}
     (gen-draggable-items ["1" "2" "3" "4" "5"])]
    [:script
     {:src
      "https://cdn.jsdelivr.net/npm/sortablejs@latest/Sortable.min.js"}]
    [:script (slurp (clojure.java.io/resource "public/js/sortable_init.js"))]]))

(defn- items [request]
  (Thread/sleep 100)
  (util/hiccup-response
   (gen-draggable-items (get-in request [:parameters :form :item]))))

(def routes
  ["/example22"
   ["" {:get {:handler main}
        :name ::main}]
   ["/items" {:post {:handler items
                     :parameters {:form {:item [:cat [:* :string]]}}}
              :name ::items}]])

(comment

  (require '[malli.core :as m])
  ;; Matches any amount of ints in a seq
  (m/validate [:cat [:* :int]] [1 2 3])
;; => false

  #_())
