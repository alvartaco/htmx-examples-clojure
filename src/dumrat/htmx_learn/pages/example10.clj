(ns dumrat.htmx-learn.pages.example10
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]))

(def ^:private get-state
  (util/get-state-or-init
   ::example10
   {:status :job/not-started
    :progress-value 0}))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    {:hx-target "this", :hx-swap "outerHTML"}
    [:h3 "Start Progress"]
    [:button {:class "btn", :hx-post (util/name->path request ::start)} "Start Job"]]))

(defn- job-complete [request]
  (util/wrap-page-hiccup
   request
   [:div
    {:hx-target "this", :hx-swap "outerHTML"}
    [:h3 "Job Completed"]
    [:button {:class "btn", :hx-post (util/name->path request ::start)} "Restart Job"]]))

(defn- job-progress [request]
  (let [{progress-value :progress-value} (deref (get-state request))]
    [:div
     {:class "progress",
      :role "progressbar",
      :aria-valuemin "0",
      :aria-valuemax "100",
      :aria-valuenow (str progress-value)
      :aria-labelledby "pblabel"}
     [:div {:id "pb", :class "progress-bar", :style (str "width:" progress-value "%")}]]))


(defn- job-progress-handler [request]
  (let [{status :status} @(get-state request)]
    (if (= status :job/completed)
      (-> (util/hiccup-response [:div])
          (assoc-in [:headers "HX-Trigger"] "done"))
      (util/hiccup-response
       (job-progress request)))))

(defn- run-job [state]
  (.start
   (Thread.
    (fn []
      (dotimes [i 10]
        (Thread/sleep (+ 300 (rand-int 500)))
        (swap! state update :progress-value #(+ % 10)))
      (swap! state assoc :status :job/completed)))))

(defn- start [request]
  (let [state (get-state request)]
    (swap! state assoc :progress-value 0)
    (swap! state assoc :status :job/in-progress)
    (run-job state)
    (util/wrap-page-hiccup
     request
     [:div
      {:hx-trigger "done",
       :hx-get (util/name->path request ::job-complete)
       :hx-swap "outerHTML",
       :hx-target "this"}
      [:h3
       {:role "status", :id "pblabel", :tabindex "-1", :autofocus ""}
       "Running"]
      [:div
       {:hx-get (util/name->path request ::job-progress)
        :hx-trigger "every 600ms",
        :hx-target "this",
        :hx-swap "innerHTML"}]])))


(def routes
  ["/example10"
   ["" {:get {:handler main}
        :name ::main}]
   ["/start" {:post {:handler start}
              :name ::start}]
   ["/job"
    ["/complete" {:get {:handler job-complete}
                  :name ::job-complete}]
    ["/progress" {:get {:handler job-progress-handler}
                  :name ::job-progress}]]])

(comment

  (require '[reitit.core])

  (->> (start {:session (atom {:example10 (atom {:status :job/not-started
                                                 :progress-value 0})})
               :reitit.core/router (reitit.ring/router routes)})
       (:body))

  (util/hiccup-response [])

  (-> (util/hiccup-response [:div])
      (assoc-in [:headers :hx-trigger] "done"))

  (str (hiccup2.core/html (:body (-> (util/hiccup-response [:div])
                                     (assoc-in [:headers :hx-trigger] "done")))))

  #_f)
