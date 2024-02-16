(ns dumrat.htmx-learn.pages.example5
  (:require [ring.util.response :as response]
            [dumrat.htmx-learn.pages.util :as util]))

(def ^:private get-state
  (util/get-state-or-init
   ::example5
   {0 {:name "Joe Smith" :email "joe@smith.org"}
    1 {:name "Angie MacDowell"  :email "angie@macdowell.org"}
    2 {:name "Fuqua Tarkenton"  :email "fuqua@tarkenton.org"}
    3 {:name "Kim Yee"  :email "kim@yee.org"}}))

(defn- row-view-raw [edit-path name email]
  [:tr
   [:td name]
   [:td email]
   [:td
    [:button
     {:class "btn btn-danger",
      :hx-get edit-path
      :hx-trigger "edit",
      :onclick
      "let editing = document.querySelector('.editing')
                         if(editing) {
                           Swal.fire({title: 'Already Editing',
                                      showCancelButton: true,
                                      confirmButtonText: 'Yep, Edit This Row!',
                                      text:'Hey!  You are already editing a row!  Do you want to cancel that edit and continue?'})
                           .then((result) => {
                                if(result.isConfirmed) {
                                   htmx.trigger(editing, 'cancel')
                                   htmx.trigger(this, 'edit')
                                }
                            })
                         } else {
                            htmx.trigger(this, 'edit')
                         }"}
     "Edit"]]])

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:table {:class "table delete-row-example"}
    [:thead
     [:tr
      [:th "Name"]
      [:th "Email"]
      [:th]]]
    [:tbody {:hx-target "closest tr", :hx-swap "outerHTML"}
     (for [[id {:keys [name email]}] (deref (get-state request))]
       (row-view-raw (util/name->path request ::row-edit {:path-params {:id id}})
                     name email))]]))

(defn- row-edit [request]
  (let [id (get-in request [:parameters :path :id])
        state @(get-state request)
        {:keys [name email]} (get state id)
        row-path (util/name->path request ::row {:path-params {:id id}})]
    (util/hiccup-response
     [:tr
      {:hx-trigger "cancel",
       :class "editing",
       :hx-get row-path}
      [:td [:input {:name "name", :value name}]]
      [:td [:input {:name "email", :value email}]]
      [:td
       [:button
        {:class "btn btn-danger", :hx-get row-path}
        "Cancel"]
       [:button
        {:class "btn btn-danger",
         :hx-put row-path
         :hx-include "closest tr"}
        "Save"]]])))

(defn- row-view [request]
  (let [id (get-in request [:parameters :path :id])
        state @(get-state request)
        {:keys [name email]} (get state id)]
    (util/wrap-page-hiccup request
                           (row-view-raw (util/name->path request ::row-edit {:path-params {:id id}})
                                         name email))))

(defn- row-update [request]
  (tap> {:in `row-update :request request})
  (let [id (get-in request [:parameters :path :id])
        state (get-state request)
        {:keys [name email]} (get-in request [:parameters :form])]
    (swap! state update id (fn [s] (-> s
                                       (assoc :name name)
                                       (assoc :email email))))
    (util/wrap-page-hiccup
     request
     (row-view-raw (util/name->path request ::row-edit {:path-params {:id id}})
                   name email))))

(def routes
  ["/example5"
   ["" {:get {:handler main}
        :name ::main}]
   ["/contact/:id"
    [""
     {:get {:handler row-view
            :parameters {:path {:id int?}}}
      :put {:handler row-update
            :parameters {:path {:id int?}
                         :form {:email string?
                                :name string?}}}
      :name ::row}]
    ["/edit" {:get {:handler row-edit
                    :parameters {:path {:id int?}}}
              :name ::row-edit}]]])

(comment

  (require '[reitit.ring :as rr]
           '[reitit.core :as rc])

  ;; TODO: Why does this get evaluated when loading the file?
  ;(util/name->path {::rc/router (rr/router routes)} ::row {:path-params {:id 0}})
  ;(rc/routes (rr/router routes))

  #_f)
