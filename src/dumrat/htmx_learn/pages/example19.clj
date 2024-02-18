(ns dumrat.htmx-learn.pages.example19
  (:require [dumrat.htmx-learn.pages.util :as util]
            [reitit.core :as rc]))

(def ^:private tab-content
  {::tab1
   "Commodo normcore truffaut VHS duis gluten-free keffiyeh iPhone taxidermy godard ramps anim pour-over. Pitchfork vegan mollit umami quinoa aute aliquip kinfolk eiusmod live-edge cardigan ipsum locavore. Polaroid duis occaecat narwhal small batch food truck. PBR&B venmo shaman small batch you probably haven't heard of them hot chicken readymade. Enim tousled cliche woke, typewriter single-origin coffee hella culpa. Art party readymade 90's, asymmetrical hell of fingerstache ipsum."
   ::tab2 "Kitsch fanny pack yr, farm-to-table cardigan cillum commodo reprehenderit plaid dolore cronut meditation. Tattooed polaroid veniam, anim id cornhole hashtag sed forage. Microdosing pug kitsch enim, kombucha pour-over sed irony forage live-edge. Vexillologist eu nulla trust fund, street art blue bottle selvage raw denim. Dolore nulla do readymade, est subway tile affogato hammock 8-bit. Godard elit offal pariatur you probably haven't heard of them post-ironic. Prism street art cray salvia."
   ::tab3 "Aute chia marfa echo park tote bag hammock mollit artisan listicle direct trade. Raw denim flexitarian eu godard etsy. Poke tbh la croix put a bird on it fixie polaroid aute cred air plant four loko gastropub swag non brunch. Iceland fanny pack tumeric magna activated charcoal bitters palo santo laboris quis consectetur cupidatat portland aliquip venmo. "})

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    {:id "tabs",
     :hx-get (util/name->path request ::tab1)
     :hx-trigger "load delay:100ms",
     :hx-target "#tabs",
     :hx-swap "innerHTML"}]))

(defn- tab [request]
  (let [path-name (get-in request [::rc/match :data :name])]
    (util/hiccup-response
     (list
      [:div
       {:class "tab-list", :role "tablist"}
       [:button
        {:hx-get (util/name->path request ::tab1)
         :class (if (= path-name ::tab1) "selected" "")
         :role "tab",
         :aria-selected "false",
         :aria-controls "tab-content"}
        "Tab 1"]
       [:button
        {:hx-get (util/name->path request ::tab2)
         :class (if (= path-name ::tab2) "selected" "")
         :role "tab",
         :aria-selected "false",
         :aria-controls "tab-content"}
        "Tab 2"]
       [:button
        {:hx-get (util/name->path request ::tab3)
         :class (if (= path-name ::tab3) "selected" "")
         :role "tab",
         :aria-selected "false",
         :aria-controls "tab-content"}
        "Tab 3"]]
      [:div
       {:id "tab-content", :role "tabpanel", :class "tab-content"}
       (path-name tab-content)]))))

(def routes
  ["/example19"
   ["" {:get {:handler main}
        :name ::main}]
   ["/tab1" {:get {:handler tab}
             :name ::tab1}]
   ["/tab2" {:get {:handler tab}
             :name ::tab2}]
   ["/tab3" {:get {:handler tab}
             :name ::tab3}]])

(comment
  #_f)
