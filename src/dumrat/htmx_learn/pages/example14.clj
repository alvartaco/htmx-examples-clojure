(ns dumrat.htmx-learn.pages.example14
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]
            [org.httpkit.client :as hk-client]
            [hickory.core :as hc]
            [hickory.select :as hs]
            [hickory.render :as hr]))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div {:hx-get (util/name->path request ::text)
          :hx-trigger "load"
          :hx-swap "outerHTML"
          :hx-indicator "#ind"}
    [:center
     [:img {:id "ind" :class "hx-indicator" :src "/assets/img/bars.svg"
            :style {:width "160px"}}]]]))

(defn- text [request]
  (let [p @(hk-client/get "https://htmx.org/examples/file-upload-input/")]
    (response/response
     (hr/hickory-to-html
      (-> (hs/select
           (hs/class "content")
           (-> p
               :body
               h/parse
               hc/as-hickory))
          first)))))

(def routes
  ["/example14"
   [""
    {:get {:handler main}
     :name ::main}]
   ["/text"
    {:get {:handler text}
     :name ::text}]])

(comment

  (require '[hickory.core :as hc]
           '[hickory.select :as hs]
           '[hickory.convert :as hcv]
           '[hickory.render :as hr])

  (def ex @(hk-client/get "https://htmx.org/examples/file-upload-input/"))

  (hr/hickory-to-html
   (-> (hs/select
        (hs/class "content")
        (-> ex
            :body
            h/parse
            hc/as-hickory))
       first))

  #_f)
