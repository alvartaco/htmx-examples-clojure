(ns dumrat.htmx-learn.pages.example1
  (:require [ring.util.response :refer [redirect]]
            [dumrat.htmx-learn.pages.util :as util]))

(def get-state
  (util/get-state-or-init
   ::example1
   {:firstName "Joe" :lastName "Blow" :email "joe@blow.com"}))

(defn- user-card [request]
  (let [;;_ (tap> {:in `user-card :state (get-state request) :request request})
        statev (deref (get-state request))]
    [:div {:hx-target "this" :hx-swap "outerHTML"}
     [:div [:label "First Name"] (str " : " (:firstName statev))]
     [:div [:label "Last Name"] (str " : " (:lastName statev))]
     [:div [:label "Email"] (str " : " (:email statev))]
     [:button {:class "btn btn-primary"
               :hx-get (util/name->path request ::contact-edit-form)} "Click to Edit"]]))

(defn- contact-view-page [request]
  (util/hiccup-response (util/wrap-page request (user-card request))))

(defn- contact-edit-form [request]
  (let [statev (deref (get-state request))]
    (util/hiccup-response
     (util/wrap-page request
                     [:form {:id "contact-frm" :hx-put "", :hx-target "this", :hx-swap "outerHTML"}
                      [:div {:class "form-group"}
                       [:label "First Name"]
                       [:input {:autofocus true :type "text", :name :firstName, :value (:firstName statev)}]]
                      [:div {:class "form-group"}
                       [:label "Last Name"]
                       [:input {:type "text", :name :lastName, :value (:lastName statev)}]]
                      [:div {:class "form-group"}
                       [:label "Email Address"]
                       [:input {:type "email", :name :email, :value (:email statev)}]]
                      [:button {:class "btn"
                                :type "submit"} "Submit"]
                      [:button {:class "btn"
                                :hx-get (util/name->path request ::contact-view-page)
                                :hx-swap "outerHTML"
                                :hx-target "#contact-frm"} "Cancel"]]))))

;;TODO : Handle the case where all form params aren't present.
(defn- contact-put [request]
  (let [params (get-in request [:parameters :form])
        state (get-state request)]
     #_(tap> {:in `contact-put :params params})
    (if params
      (do (reset! state (reduce (partial apply assoc) @state params))
          (redirect
           (util/name->path request ::contact-view-page)
           :see-other)))))

(defn- contact-main [request]
  (redirect
   (util/name->path request ::contact-view-page)
   :see-other))

(def routes
  ["/example1"
   ["" {:name ::main
        :handler contact-main}]
   ["/contact/1"
    {:name ::contact-view-page
     :get {:handler contact-view-page}
     :put {:handler contact-put
           :parameters {:form [:map
                               [:firstName string?]
                               [:lastName string?]
                               [:email string?]]}}}]
   ["/contact/1/edit-form"
    {:name ::contact-edit-form
     :get {:handler contact-edit-form}}]])


(comment

  (str :firstName)

  #_f)
