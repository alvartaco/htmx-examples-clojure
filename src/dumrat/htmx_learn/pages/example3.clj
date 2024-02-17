(ns dumrat.htmx-learn.pages.example3
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]))

(defn- get-random-str []
  (second (re-matches #"(.*?)-.*" (str (random-uuid)))))

(def ^:private get-state
  (util/get-state-or-init
   ::example3
   {:count 100
    :page-size 10
    :entries (map (fn [i]
                    {:name (format "Agent %03d" i)
                     :email (format "void%03d@null.org" i)
                     :id (get-random-str)})
                  (range 100))}))

;;TODO: Perhaps I shouldn't pass request into this fn.
(defn- get-page [request ^Integer page]
  #_(tap> {:in `get-page :request request :page page})
  (let [{cnt :count page-size :page-size entries :entries} @(get-state request)
        _ (tap> [cnt page-size])
        has-more-to-load? (> cnt (* page-size (dec page)))
        next-page (if has-more-to-load? (inc page) page)]
    (lazy-cat
     (when has-more-to-load?
      (for [{:keys [name email id]} (take page-size (drop (* page-size page) entries))]
        [:tr
         [:td name]
         [:td email]
         [:td id]]))
     [[:tr {:id "replaceMe"}
       [:td {:colspan 3}
        [:center
         [:button {:class "btn"
                   :hx-get (util/name->path request ::page {:query-params {:page next-page}})
                   :hx-target "#replaceMe"
                   :hx-swap "outerHTML"}
          "Load More Agents..."
          [:img {:class "htmx-indicator" :src "/assets/img/bars.svg"}]]]]]])))

(defn- example3-view [request]
  (util/wrap-page-hiccup
   request
   [:table
    [:thead
     [:tr
      [:th "Name"]
      [:th "Email"]
      [:th "ID"]]]
    (into [:tbody {:id "tbody"}]
          (get-page request 0))]))

(defn- page [request]
  #_(tap> {:in `page :request request})
  (Thread/sleep 300) ;; Emulate data load delay
  (let [requested-page (get-in request [:parameters :query :page])]
    (if-not requested-page
      (response/bad-request "Required parameter page not specified")
      (util/hiccup-response
       (get-page request requested-page)))))

(def routes
  ["/example3"
   ["" {:get {:handler example3-view}
        :name ::main}]
   ["/page" {:get {:handler page
                   :parameters {:query {:page int?}}}
             :name ::page}]])

(comment

  (str (random-uuid))
  (re-matches #"(.*?)-.*" (str (random-uuid)))
  (get-random-str)

  (Integer/parseInt "10")

  (class (for [i (range 3)] i))

  (let [s (lazy-seq (take 10 (range)))]
    (lazy-cat s [99]))

  (util/name->path {:reitit.core/router (reitit.ring/router routes)}
                   ::page {:query-params {"page" 2}})

  (into list (for [i (range 10)] i))

  (lazy-cat nil nil)

  #_f)
