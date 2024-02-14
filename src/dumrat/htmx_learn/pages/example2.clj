(ns dumrat.htmx-learn.pages.example2
  (:require [dumrat.htmx-learn.pages.util :refer [wrap-page hiccup-response get-state-or-init name->path]]
            [ring.util.response :as response]))

(def get-state
  (get-state-or-init
   "example2"
   [{:name "Joe Smith" :email "joe@smith.org" :active true}
    {:name "Angie MacDowell"  :email "angie@macdowell.org" :active true}
    {:name "Fuqua Tarkenton"  :email "fuqua@tarkenton.org" :active false}
    {:name "Kim Yee"  :email "kim@yee.org" :active false}]))

(defn- view [request]
  (hiccup-response
   (wrap-page
    request
    [:form {:id "checked-contacts" :hx-post "" :hx-target "this" :hx-swap "outerHTML"}
     [:table
      [:thead
       [:tr
        [:th "Name"]
        [:th "Email"]
        [:th "Active"]]]
      [:tbody {:id "tbody"}
       (for [{:keys [name email active]} (deref (get-state request))]
         [:tr
          [:td name]
          [:td email]
          [:td
           [:input {:type "checkbox", :name (str "active:" email) :checked active}]]])]]
     [:input.btn {:type "submit" :value "Bulk update"}]
     [:span {:id "toast"}]])))

(defn- update [request]
  (let [update-vals (:form-params request)
        next-vals
        (map (fn [{:keys [email] :as curr}]
               (assoc curr :active (update-vals (str "active:" email))))
             (deref (get-state request)))]
    (reset! (get-state request) next-vals)
    (response/redirect (name->path request ::main) :see-other)))

(def routes
  ["/example2"
   ["" {:get {:handler view}
        :post {:handler update}
        :name ::main}]])
