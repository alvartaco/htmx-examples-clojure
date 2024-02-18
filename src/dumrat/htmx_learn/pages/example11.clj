(ns dumrat.htmx-learn.pages.example11
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]
            [reitit.core :as r]))

(def ^:private get-state
  (util/get-state-or-init
   ::example11
   {:makes [{:value "audi" :caption "Audi"}
            {:value "toyota" :caption "Toyota"}
            {:value "bmw" :caption "BMW"}]
    :models {"audi" [{:value "a1" :caption "A1"}
                     {:value "a3" :caption "A3"}
                     {:value "a6" :caption "A6"}]
             "toyota" [{:value "landcruiser" :caption "Landcruiser"}
                       {:value "tacoma" :caption "Tacoma"}
                       {:value "yaris" :caption "Yaris"}]
             "bmw" [{:value "325i" :caption "325i"}
                    {:value "325ix" :caption "325ix"}
                    {:value "x5" :caption "X5"}]}}))

(defn- get-models [request]
  (let [make (get-in request [:parameters :query :make])
        models (get-in (deref (get-state request)) [:models make])]
    (map (fn [{value :value caption :caption}]
           [:option {:value value} caption])
         models)))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   (let [{makes :makes} (deref (get-state request))]
     [:div
      [:label " Make"]
      [:select {:name "make"
                :hx-trigger "change"
                :hx-get (util/name->path request ::models)
                :hx-target "#models"
                :hx-indicator ".htmx-indicator"}
       (map (fn [{value :value caption :caption}] [:option {:value value} caption])
            makes)]
      [:div
       [:label "Model"]
       [:select {:id "models" :name "model"}
        ;;Hacky
        (get-models (assoc-in request [:parameters :query :make] "audi"))]]])))

(defn- models-handler [request]
  (util/hiccup-response
   (get-models request)))

(def routes
  ["/example11"
   ["" {:get {:handler main}
        :name ::main}]
   ["/models" {:get {:handler models-handler
                     :parameters {:query {:make string?}}}
               :name ::models}]])

(comment


  #_f)
