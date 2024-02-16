(ns dumrat.htmx-learn.pages.example7
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]
            [garden.core :refer [css]]))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   (list
    [:h3 "Signup Form"]
    [:form
     {:hx-post "/contact"}
     [:div
      {:hx-target "this", :hx-swap "outerHTML"}
      [:label "Email Address"]
      [:input
       {:name "email", :hx-post (util/name->path request ::validate-email) :hx-indicator "#ind"}]
      [:img {:id "ind", :src "/assets/img/bars.svg", :class "htmx-indicator"}]]
     [:div
      {:class "form-group"}
      [:label "First Name"]
      [:input {:type "text", :class "form-control", :name "firstName"}]]
     [:div
      {:class "form-group"}
      [:label "Last Name"]
      [:input {:type "text", :class "form-control", :name "lastName"}]]
     [:button {:class "btn btn-default"} "Submit"]])))

(defn- validate-email [request]
  (let [{email :email} (get-in request [:parameters :form])
        error? (not= email "test@test.com")]
    (Thread/sleep 500)
    (util/hiccup-response
     (cond->
         [:div
          (cond-> {:hx-target "this" :hx-swap "outerHTML" :class "valid"}
            error? (assoc :class "error"))
          [:label "Email Address"]
          [:input
           {:name "email",
            :hx-post (util/name->path request ::validate-email)
            :hx-indicator "#ind",
            :value email}]
          [:img {:id "ind", :src "/assets/img/bars.svg", :class "htmx-indicator"}]]
         error? (conj [:div
                       {:class "error-message"}
                       "That email is already taken.  Please enter another email."])))))

(def routes
  ["/example7"
   ["" {:get {:handler main}
        :name ::main}]
   ["/contact/validate-email" {:post {:handler validate-email
                                      :parameters {:form {:email string? :firstName string? :lastName string?}}}
                               :name ::validate-email}]])

(comment

  (merge {:a 1} nil)

  #_f)
