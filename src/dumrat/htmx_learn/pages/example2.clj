(ns dumrat.htmx-learn.pages.example2
  (:require [dumrat.htmx-learn.pages.util :refer [] :as util]
            [ring.util.response :as response]))

(def ^:private get-state
  (util/get-state-or-init
    ::example2
   [{:name "Joe Smith" :email "joe@smith.org" :active true}
    {:name "Angie MacDowell"  :email "angie@macdowell.org" :active true}
    {:name "Fuqua Tarkenton"  :email "fuqua@tarkenton.org" :active false}
    {:name "Kim Yee"  :email "kim@yee.org" :active false}]))

(defn- example2-view [request]
  (util/wrap-page-hiccup
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
    [:center
     [:input {:class "btn" :type "submit" :value "Bulk update"}]]
    [:span {:id "toast"}]]))

(defn- example2-update [request]
  (let [update-vals (:params request)
        ;;_ (tap> {:in `example2-update :update-vals update-vals :request request})
        next-vals
        (map (fn [{:keys [email] :as curr}]
               (assoc curr :active (update-vals (str "active:" email))))
             (deref (get-state request)))]
    (reset! (get-state request) next-vals)
    (response/redirect (util/name->path request ::main) :see-other)))

(def routes
  ["/example2"
   ["" {:get {:handler example2-view}
        :post {:handler example2-update}
        :name ::main}]])
