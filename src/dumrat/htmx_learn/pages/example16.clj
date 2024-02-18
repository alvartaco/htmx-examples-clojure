(ns dumrat.htmx-learn.pages.example16
  (:require [dumrat.htmx-learn.pages.util :as util]))

(defn- main [request]
  (util/wrap-page-hiccup
   request
   [:div
    [:button
     {:id "showButton",
      :hx-get (util/name->path request ::modal)
      :hx-target "#modals-here",
      :class "uk-button uk-button-primary",
      :_ "on htmx:afterOnLoad wait 10ms then add .uk-open to #modal"}
     "Open Modal"]
    [:div {:id "modals-here"}]
    [:style "@import 'https://cdnjs.cloudflare.com/ajax/libs/uikit/3.5.9/css/uikit-core.min.css';"]]))

(defn- modal [request]
  (util/hiccup-response
   [:div
    {:id "modal", :class "uk-modal", :style "display:block;"}
    [:div
     {:class "uk-modal-dialog uk-modal-body"}
     [:h2 {:class "uk-modal-title"} "Modal Dialog"]
     [:p "This modal dialog was loaded dynamically by HTMX."]
     ;;TODO: this target doesn't make sense.
     [:form {:hx-post (util/name->path request ::modal)
             :hx-target "this"
             :hx-swap "outerHTML"
             :_ "on submit take .uk-open from #modal"}
      [:div
       {:class "uk-margin"}
       [:input {:class "uk-input", :placeholder "What is Your Name?" :name "name"}]]
      [:button
       {:type "submit", :class "uk-button uk-button-primary"}
       "Save Changes"]
      [:button
       {:id "cancelButton",
        :type "button",
        :class "uk-button uk-button-default",
        :_ "on click take .uk-open from #modal wait 200ms then remove #modal"}
       "Close"]]]]))

(defn- modal-post [request]
  (tap> [`modal-post "called"])
  (util/hiccup-response [:div "Called to server"]))

(def routes
  ["/example16"
   [""
    {:get {:handler main}
     :name ::main}]
   ["/modal"
    {:get {:handler modal}
     :post {:handler modal-post}
     :name ::modal}]])

(comment

  @import "https://cdnjs.cloudflare.com/ajax/libs/uikit/3.5.9/css/uikit-core.min.css";

  #_f)
