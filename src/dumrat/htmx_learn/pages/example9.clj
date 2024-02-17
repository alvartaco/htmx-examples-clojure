(ns dumrat.htmx-learn.pages.example9
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]
            [talltale.core :as tt]
            [clojure.string :as str]
            [ws.clojure.extensions :as ws]))

(defn- generate-sample-data [num-samples]
  (mapv (fn [_] (select-keys (tt/person) [:first-name :last-name :email]))
        (range num-samples)))

(defonce ^:private sample-data (atom nil))

(defn- get-data []
  (when-not @sample-data
    (reset! sample-data (generate-sample-data 1000)))
  @sample-data)

;; TODO: This is lower-casing all entries each request.
(defn- find-matching-entries [search-str]
  (if search-str
    (let [search-str (str/lower-case search-str)]
      (filter (fn [entry]
                (some (ws/flip str/includes? search-str) (map str/lower-case (vals entry))))
              (get-data)))
    ()))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   (list
    [:h3
     "Search Contacts"
     [:span
      {:class "htmx-indicator" :style {:margin-left "1em"}}
      [:img {:src "/assets/img/bars.svg"}]
      "Searching..."]]
    [:input
     {:class "form-control",
      :type "search",
      :name "search",
      :placeholder "Begin Typing To Search Users...",
      :hx-post (util/name->path request ::search)
      :hx-trigger "input changed delay:500ms, search",
      :hx-target "#search-results",
      :hx-indicator ".htmx-indicator"}]
    [:table
     {:class "table"}
     [:thead [:tr [:th "First Name"] [:th "Last Name"] [:th "Email"]]]
     [:tbody {:id "search-results"}]])))

(defn- search [request]
  (Thread/sleep 500)
  (let [search-str (get-in request [:parameters :form :search])
        search-results (find-matching-entries search-str)]
    (util/hiccup-response (map (fn [{first-name :first-name last-name :last-name email :email}]
                                 [:tr
                                  [:td first-name]
                                  [:td last-name]
                                  [:td email]])
                               search-results))))

(def routes
  ["/example9"
   ["" {:get {:handler main}
        :name ::main}]
   ["/search" {:post {:handler search
                      :parameters {:form {:search string?}}}
               :name ::search}]])

(comment

  (merge {:a 1} nil)

  (generate-sample-data 10)

  (let [persons (generate-sample-data 1000)
        person (select-keys (tt/person) [:first-name :last-name :email])
        str "ab"]
    (filter #(some (ws/flip str/includes? str) (vals %)) persons))

  (find-matching-entries nil)

  #_f)
