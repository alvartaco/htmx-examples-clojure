(ns dumrat.htmx-learn.pages.example20
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
     :hx-target "#tab-contents",
     :hx-swap "outerHTML"
     :role "tablist"
     "hx-on:htmx-after-on-load"
     (str "let currentTab = document.querySelector('[aria-selected=true]');"
          "currentTab.setAttribute('aria-selected', 'false');"
          "currentTab.classList.remove('selected');"
          "let newTab = event.target;"
          "newTab.setAttribute('aria-selected', 'true');"
          "newTab.classList.add('selected');")}
    (list
     [:button
      {:hx-get (util/name->path request ::tab1)
       :class "selected"
       :role "tab",
       :aria-selected "true"
       :aria-controls "tab-content"}
      "Tab 1"]
     [:button
      {:hx-get (util/name->path request ::tab2)
       :role "tab",
       :aria-selected "false"
       :aria-controls "tab-content"}
      "Tab 2"]
     [:button
      {:hx-get (util/name->path request ::tab3)
       :role "tab",
       :aria-selected "false"
       :aria-controls "tab-content"}
      "Tab 3"]
     [:div
      {:id "tab-contents", :role "tabpanel"}
      (::tab1 tab-content)])]))

(defn- tab [request]
  (let [path-name (get-in request [::rc/match :data :name])]
    (util/hiccup-response
     (list
      [:div
       {:id "tab-contents", :role "tabpanel"}
       (path-name tab-content)]))))

(def routes
  ["/example20"
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
