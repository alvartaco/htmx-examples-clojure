(ns dumrat.htmx-learn.pages.example6
  (:require [dumrat.htmx-learn.pages.util :refer [] :as util]
            [reitit.core :as rc]
            [ring.util.response :as response]
            [org.httpkit.client :as hk-client]
            [babashka.fs :as fs]))

(def ^:private get-state
  (util/get-state-or-init
   ::example6
   {:time-to-cache 60 ;;minutes
    :url "https://api.star-history.com/svg?repos=bigskysoftware/htmx&type=Date"
    :expire-at (java.time.LocalDateTime/now)
    :local-path "dynamic/img/htmx_stars.svg"}))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:center
    [:div
     {:hx-get (util/name->path request ::stars)
      :hx-trigger "load"}
     [:img
      {:alt "Result loading..."
       :class "htmx-indicator"
       :width "150px"
       :src "/assets/img/bars.svg"}]]]))

(defn- stars
  [request]
  (let [state (get-state request)
        ;_ (tap> {:in `stars :state state})
        {:keys [time-to-cache expire-at local-path url]} @state]
    (when (.isBefore expire-at (java.time.LocalDateTime/now))
      (let [response @(hk-client/get url)
            content (:body response)
            target-filename (str (fs/path (fs/cwd) "resources/public" local-path))]
        #_(tap> {:in `stars :response response :content content :cwd (fs/cwd)})
        (fs/create-dirs (fs/parent target-filename))
        (spit target-filename content)
        (swap! state assoc :expire-at (.plusMinutes (java.time.LocalDateTime/now) time-to-cache))))
    (Thread/sleep 2000)
    (response/resource-response local-path {:root "/public"})))

(def routes
  ["/example6"
   ["" {:get {:handler main}
        :name ::main}]
   ["/stars" {:get {:handler stars}
              :name ::stars}]])

(comment

  (class (java.time.LocalDateTime/now))

  (let [t1 (java.time.LocalDateTime/now)
        t2 (do (Thread/sleep 20) (java.time.LocalDateTime/now))
        r1 (.compareTo t1 t2)
        r2 (.isAfter t1 t2)
        r3 (.isBefore t1 t2)
        r4 (.isEqual t1 t2)]
    [r1 r2 r3 r4])

  (def resp-promise
    (hk-client/get
     "https://api.star-history.com/svg?repos=bigskysoftware/htmx&type=Date"))

  @resp-promise

  (require '[reitit.core :as rc])

  (ns-publics 'org.httpkit.client)

  (let [routes [:hi
                [:hello ::hoo]]
        router (rc/router routes)]
    (rc/routes router))

  (require '[clojure.repl])

  (tap> (with-out-str (clojure.repl/doc hk-client/get)))

  (response/resource-response "/img/htmx_stars.svg" {:root "/public"})

  (str (fs/path (fs/cwd) "resources/public/img/htmx_stars.svg"))

  (ns-publics 'babashka.fs)

  #_f)
