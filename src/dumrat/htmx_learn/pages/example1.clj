(ns dumrat.htmx-learn.pages.example1
  (:require [ring.util.response :refer [redirect]]
            [dumrat.htmx-learn.pages.util :refer [name->path hiccup->html page]]))

(defonce ^:private state
  (atom {::first-name "Joe" ::last-name "Blow" ::email "joe@blow.com"}))

(defn- user-card [request]
  [:div {:hx-target "this" :hx-swap "outerHTML"}
   [:div [:label "First Name"] (str " : " (::first-name @state))]
   [:div [:label "Last Name"] (str " : " (::last-name @state))]
   [:div [:label "Email"] (str " : " (::email @state))]
   [:button.btn.btn-primary {:hx-get (name->path request ::contact-edit-form)} "Click to Edit"]])

(defn contact-view-page [request]
  {:status  200
   :headers {}
   :body (hiccup->html (page (user-card request)))})

(defn contact-edit-form [request]
  {:status 200
   :headers {}
   :body (hiccup->html
          (page [:form {:hx-put "", :hx-target "this", :hx-swap "outerHTML"}
                 [:div
                  [:label "First Name"]
                  [:input {:autofocus true :type "text", :name "firstName", :value (::first-name @state)}]]
                 [:div.form-group
                  [:label "Last Name"]
                  [:input {:type "text", :name "lastName", :value (::last-name @state)}]]
                 [:div {:class "form-group"}
                  [:label "Email Address"]
                  [:input {:type "email", :name "email", :value (::email @state)}]]
                 [:button.btn {:type "submit"} "Submit"]
                 [:button.btn {:hx-get (name->path request ::contact-view-page)} "Cancel"]]))})

(def html-key->state-key
  {"firstName" ::first-name "lastName" ::last-name "email" ::email})

(defn contact-put [request]
  (let [params (:form-params request)]
    (if (every? some? params)
      (do (swap! state
                 (fn [s]
                   (reduce (partial apply assoc) s
                           (update-keys params html-key->state-key))))
          (redirect
           (name->path request ::contact-view-page)
           :see-other)))))

(def routes
  ["/example1"
   ["/contact/1"
    {:name ::contact-view-page
     :get {:handler contact-view-page}
     :put {:handler contact-put}}]
   ["/contact/1/edit-form"
    {:name ::contact-edit-form
     :get {:handler contact-edit-form}}]])
