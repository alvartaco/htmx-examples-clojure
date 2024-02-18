(ns dumrat.htmx-learn.pages.example12
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]
            [reitit.core :as r]))

(def ^:private get-state
  (util/get-state-or-init
   ::example12
   {:next-color-map {:red :blue
                     :blue :green
                     :green :yellow
                     :yellow :brown
                     :brown :pink
                     :pink :red}
    :current-color :pink}))

(defn- colors [request]
  (util/hiccup-response
   (let [state (get-state request)
         {next-color-map :next-color-map current-color :current-color} @state
         next-color (next-color-map current-color)]
     (swap! state assoc :current-color next-color)
     [:div {:id "color-demo" :class "smooth" :style {:color next-color}
            :hx-get (util/name->path request ::colors) :hx-swap "outerHTML" :hx-trigger "every 1s"}
      "Color Swap Demo"])))

(defn- fade-out-view [request]
  (util/hiccup-response
   [:button {:class "fade-me-out" :hx-delete (util/name->path request ::fade-out) :hx-swap "outerHTML swap:1s"}
    "Fade Me Out"]))

(defn- fade-out-delete [_]
  (response/response ""))

(defn- fade-in [request]
  (util/hiccup-response
   [:button
    {:id "fade-me-in"
     :hx-post (util/name->path request ::fade-in)
     :hx-swap "outerHTML settle:1s"}
    "Fade Me In"]))


(defn- in-flight-view [request]
  (util/hiccup-response
   [:form {:hx-post (util/name->path request ::in-flight) :hx-swap "outerHTML"}
    [:label "Name: "]
    [:input {:name "name"}]
    [:br]
    [:button "Submit"]]))

(defn- in-flight-post [_]
  (Thread/sleep 1000)
  (util/hiccup-response
   [:span "Submitted!"]))

;;TODO: Do the view transition stuff. But I'm on FF and I don't see them.

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    [:div
     [:label "Color Throb"]
     [:br]
     [:div {:hx-trigger "load" :hx-get (util/name->path request ::colors) :hx-swap "outerHTML"}]]
    [:br]
    [:div
     [:label "Fade Out On Swap"]
     [:br]
     [:div {:hx-trigger "load" :hx-get (util/name->path request ::fade-out) :hx-swap "outerHTML"}]]
    [:br]
    [:div
     [:label "Fade In On Addition"]
     [:br]
     [:div {:hx-trigger "load" :hx-get (util/name->path request ::fade-in) :hx-swap "outerHTML"}]]
    [:br]
    [:div
     [:label "Request In Flight Animation"]
     [:br]
     [:div {:hx-trigger "load" :hx-get (util/name->path request ::in-flight) :hx-swap "outerHTML"}]]]))

(def routes
  ["/example12"
   ["" {:get {:handler main}
        :name ::main}]
   ["/colors" {:get {:handler colors}
               :name ::colors}]
   ["/fade-out" {:get {:handler fade-out-view}
                 :delete {:handler fade-out-delete}
                 :name ::fade-out}]
   ["/fade-in" {:get {:handler fade-in}
                 :post {:handler fade-in}
                 :name ::fade-in}]
   ["/in-flight" {:get {:handler in-flight-view}
                  :post {:handler in-flight-post}
                  :name ::in-flight}]])

(comment

  #_f)
