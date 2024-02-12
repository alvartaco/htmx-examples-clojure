(ns dumrat.htmx-learn.pages.example1
  (:require [hiccup2.core :as hiccup]
            [ring.util.response :as response]))

(def ^:private state
  ^{:doc "Store state in app scope atom"}
  (atom {:firstName "Joe"
         :lastName "Blow"
         :email "joe@blow.com"}))

(defn user-card []
  [:div {:hx-target "this" :hx-swap "outerHTML"}
   [:div [:label "First Name"] (str " : " (:firstName @state))]
   [:div [:label "Last Name"] (str " : " (:lastName @state))]
   [:div [:label "Email"] (str " : " (:email @state))]
   [:button {:hx-get "/htmx-examples/example1/contact/1/edit-form"} "Click to Edit"]])

(defn contact-view-page [request]
  {:status  200
   :headers {}
   :body
   (str
    (hiccup/html
     [:html
      [:head
       [:meta {:charset "utf-8"}]
       [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
       [:meta {:name "description" :content "This is an implementation for htmx official example 1 - Click to Edit with a Clojure back-end."}]
       [:title "</> htmx ~ Examples ~ Click to Edit"]
       [:link {:rel "stylesheet" :href "/public/css/site.css"}]
       [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]]
      [:body
       (user-card)]]))})

(defn contact-edit-form [request]
  {:status 200
   :headers {}
   :body
   (str (hiccup/html
         [:form {:hx-put "", :hx-target "this", :hx-swap "outerHTML"}
          [:div
           [:label "First Name"]
           [:input {:type "text", :name "firstName", :value (:firstName @state)}]]
          [:div {:class "form-group"}
           [:label "Last Name"]
           [:input {:type "text", :name "lastName", :value (:lastName @state)}]]
          [:div {:class "form-group"}
           [:label "Email Address"]
           [:input {:type "email", :name "email", :value (:email @state)}]]
          [:button {:class "btn"} "Submit"]
          [:button {:class "btn", :hx-get "/htmx-examples/example1/contact/1"} "Cancel"]]))})

(defn contact-put [request]
  (let [[firstName lastName email :as params]
        (map #(get-in request [:form-params %]) ["firstName" "lastName" "email"])]
    (if (every? some? params)
      (do (swap! state
                 (fn [s]
                   (-> s
                       (assoc :firstName firstName)
                       (assoc :lastName lastName)
                       (assoc :email email))))
          (response/redirect
            "/htmx-examples/example1/contact/1"
            :see-other)))))

(def routes
  ["/example1"
   ["/contact/1"
    {:get {:name ::contact-view-page
           :handler contact-view-page}
     :put {:name ::contact-put
           :handler contact-put}}]
   ["/contact/1/edit-form"
    {:get {:name ::contact-edit-form
           :handler contact-edit-form}}]])
