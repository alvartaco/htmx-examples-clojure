(ns dumrat.htmx-learn.pages.example4
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]
            [reitit.core :as r]))

(def ^:private get-state
  (util/get-state-or-init
   ::example4
   {0 {:name "Joe Smith" :email "joe@smith.org" :status true}
    1 {:name "Angie MacDowell"  :email "angie@macdowell.org" :status true}
    2 {:name "Fuqua Tarkenton"  :email "fuqua@tarkenton.org" :status false}
    3 {:name "Kim Yee"  :email "kim@yee.org" :status false}}))

(defn- view [request]
  (util/hiccup-response
   (util/wrap-page
    request
    [:table {:class "table delete-row-example"}
     [:thead
      [:tr
       [:th "Name"]
       [:th "Email"]
       [:th "Status"]
       [:th]]]
     [:tbody
      {:id "tbody"
       :hx-confirm "Are you sure you want to delete this user?"
       :hx-target "closest tr"
       :hx-swap "outerHTML swap:1s"}
      (for [[id {:keys [name email status]}] (deref (get-state request))]
        [:tr
         [:td name]
         [:td email]
         [:td status]
         [:td
          [:button {:class "btn btn-danger"
                    :hx-delete (util/name->path request ::delete {:path-params {:id id}})}
           "Delete"]]])]])))

(defn- delete [request]
  (let [delete-id (get-in request [:parameters :path :id])
        state (get-state request)
        _ (tap> {:in `example4-update :request request :delete-id delete-id})
        ]
    (when delete-id
      (swap! state dissoc delete-id))
    (response/response nil)))

(def routes
  ["/example4"
   ["" {:get {:handler view}
        :name ::main}]
   ["/contact/:id" {:delete {:handler delete
                             :parameters {:path {:id int?}}}
                    :name ::delete}]])

(comment

  (require '[reitit.ring :as rr])
  (require '[reitit.core :as r])

  (r/match-by-path (rr/router routes) "/example4/contact/1")

  (util/name->path (rr/router routes) ::delete {:id 1})
  (r/match-by-name (rr/router routes) ::delete {:id 1})

  #_f)
