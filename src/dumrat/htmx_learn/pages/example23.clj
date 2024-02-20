(ns dumrat.htmx-learn.pages.example23
  (:require [dumrat.htmx-learn.pages.util :as util]
            [ring.util.response :as response]))

(def ^:private get-state
  (util/get-state-or-init
   ::example23
   []))

(defn solution1 [request]
  (let [state @(get-state request)]
    [:div {:id "table-and-form"}
     [:h2 "Contacts 1"]
     [:table
      {:class "table"}
      [:thead [:tr [:th "Name"] [:th "Email"] [:th]]]
      [:tbody {:id "contacts-table"}
       (map (fn [{name :name email :email}]
              [:tr
               [:td name]
               [:td email]])
            state)]]
     [:h2 "Add A Contact"]
     [:form
      {:hx-post (util/name->path request ::add-contact1)
       :hx-target "#table-and-form"}
      [:label "Name" [:input {:name "name", :type "text"}]]
      [:label "Email" [:input {:name "email", :type "email"}]]
      [:button {:type "submit"} "Add"]]]))

(defn solution2 [request]
  (let [state @(get-state request)]
    (list
     [:h2 "Contacts 2"]
     [:table
      {:class "table"}
      [:thead [:tr [:th "Name"] [:th "Email"] [:th]]]
      [:tbody {:id "contacts-table"}
       (map (fn [{name :name email :email}]
              [:tr
               [:td name]
               [:td email]])
            state)]]
     [:h2 "Add A Contact"]
     [:form
      {:hx-post (util/name->path request ::add-contact2)}
      [:label "Name" [:input {:name "name", :type "text"}]]
      [:label "Email" [:input {:name "email", :type "email"}]]
      [:button {:type "submit"} "Add"]])))

(defn solution3 [request]
  (let [state @(get-state request)]
    (list
     [:h2 "Contacts 3"]
     [:table {:id "contacts-table" :class "table"}
      [:thead [:tr [:th "Name"] [:th "Email"] [:th]]]
      [:tbody {:id "contacts-table"
               :hx-get (util/name->path request ::table3)
               :hx-trigger "newContact from:body"
               :hx-target "#contacts-table"}
       (map (fn [{name :name email :email}]
              [:tr
               [:td name]
               [:td email]])
            state)]]
     [:h2 "Add A Contact"]
     [:form
      {:hx-post (util/name->path request ::add-contact3) :hx-swap "none"}
      [:label "Name" [:input {:name "name", :type "text"}]]
      [:label "Email" [:input {:name "email", :type "email"}]]
      [:button {:type "submit"} "Add"]])))

(def solutions
  {1 solution1
   2 solution2
   3 solution3})

(defn- main [request]
  (util/wrap-page-hiccup
   request
   (let [solution-num (Integer/parseInt (or (get-in request [:query-params "solution"]) "1"))]
     [:div {:id "solution-container"}
      [:select {:hx-trigger "change" :hx-target "#solution-container"
                :hx-get (util/name->path request ::main)
                :name "solution"}
       (map
        (fn [num]
          [:option
           (cond-> {:name (str "solution" num) :value num}
             (= num solution-num) (assoc :selected "selected"))
           (str "Solution " num)])
        (keys solutions))]
      ((solutions solution-num) request)
      [:script "htmx.onLoad(function(content) {htmx.config.useTemplateFragments = true;})"]])))

(defn- add-contact1 [request]
  (let [state (get-state request)
        entry (get-in request [:parameters :form])]
    (swap! state conj entry)
    (util/hiccup-response
     (solution1 request))))

(defn- add-contact2 [request]
  (let [state (get-state request)
        {name :name email :email :as entry} (get-in request [:parameters :form])]
    (swap! state conj entry)
    (util/hiccup-response
     (list
      [:tr {:hx-swap-oob "beforeend:#contacts-table"}
       [:td name]
       [:td email]]
      [:form
       {:hx-post (util/name->path request ::add-contact2)}
       [:label "Name" [:input {:name "name", :type "text"}]]
       [:label "Email" [:input {:name "email", :type "email"}]]
       [:button {:type "submit"} "Add"]]))))

(defn- table3 [request]
  (let [state @(get-state request)]
    (util/hiccup-response
     [:table {:id "contacts-table" :class "table"}
      [:thead [:tr [:th "Name"] [:th "Email"] [:th]]]
      [:tbody {:hx-get (util/name->path request ::table3)
               :hx-trigger "newContact from:body"
               :hx-target "#contacts-table"}
       (map (fn [{name :name email :email}]
              [:tr
               [:td name]
               [:td email]])
            state)]])))

(defn- add-contact3 [request]
  (let [state (get-state request)
        entry (get-in request [:parameters :form])]
    (swap! state conj entry))
  {:status 200 :headers {"Hx-Trigger" "newContact"} :body ""})

(def routes
  ["/example23"
   ["" {:get {:handler main}
        :name ::main}]
   ["/contact1" {:post {:handler add-contact1
                        :parameters {:form {:name :string
                                            :email :string}}}
                 :name ::add-contact1}]
   ["/contact2" {:post {:handler add-contact2
                        :parameters {:form {:name :string
                                            :email :string}}}
                 :name ::add-contact2}]
   ["/table3" {:get {:handler table3}
               :name ::table3}]
   ["/contact3" {:post {:handler add-contact3
                        :parameters {:form {:name :string
                                            :email :string}}}
                 :name ::add-contact3}]])

(comment
  #_())
