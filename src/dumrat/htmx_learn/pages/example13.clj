(ns dumrat.htmx-learn.pages.example13
  (:require
   [dumrat.htmx-learn.pages.util :as util]
   [reitit.ring.malli :as malli]
   [ring.util.response :as response]
   [clojure.java.io :as io]))

(def ^:private xhr-script
  "htmx.on('#form',
           'htmx:xhr:progress',
           function(evt) {\n htmx.find('#progress1').setAttribute('value', evt.detail.loaded/evt.detail.total * 100);\n console.log(\"hi\")});")

(defn- view1 [request]
  (util/wrap-page-hiccup
   request
   (list
    [:form
     {:id "form", :hx-encoding "multipart/form-data", :hx-post (util/name->path request ::upload1)}
     [:input {:type "file", :name "file1"}]
     [:button "Upload"]
     [:progress {:id "progress1", :value "0", :max "100"}]]
    [:script xhr-script
     ])))

(defn- view2 [request]
  (util/wrap-page-hiccup
   request
   [:form
    {:hx-encoding "multipart/form-data"
     :hx-post (util/name->path request ::upload2)
     :_ "on htmx:xhr:progress(loaded, total) set #progress2.value to (loaded/total)*100"}
    [:input {:type "file", :name "file2"}]
    [:button "Upload"]
    [:progress {:id "progress2", :value "0", :max "100"}]]))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    [:div
     [:label "With js"]
     [:div {:hx-get (util/name->path request ::view1)
            :hx-trigger "load"
            :hx-swap "outerHTML"}]]
    [:div
     [:label "With hyperscript"]
     [:div {:hx-get (util/name->path request ::view2)
            :hx-trigger "load"
            :hx-swap "outerHTML"}]]]))

(defn- upload [request]
  (let [{tempfile :tempfile} request]
    (util/hiccup-response [:div "Upload successful"])))


(def routes
  ["/example13"
   ["" {:get {:handler main}
        :name ::main}]
   ["/view1" {:get {:handler view1}
              :name ::view1}]
   ["/view2" {:get {:handler view2}
              :name ::view2}]
   ["/upload1" {:post {:handler upload
                       :parameters {:multipart {:file1 malli/temp-file-part}}}
                :name ::upload1}]
   ["/upload2" {:post {:handler upload
                       :parameters {:multipart {:file2 malli/temp-file-part}}}
                :name ::upload2}]])

(comment

  #_f)
