(ns dumrat.htmx-learn.pages.example17
  (:require [dumrat.htmx-learn.pages.util :as util]))

;;TODO: Doesn't work properly. No modal shown. Probably I'm missing some bootstrap init call?

(defn- main [request]
  (util/wrap-page-hiccup
   request
   (list
    [:button
     {:hx-get (util/name->path request ::modal)
      :hx-target "#modals-here",
      :hx-trigger "click",
      :data-bs-toggle "modal",
      :data-bs-target "#modals-here",
      :class "btn btn-primary"}
     "Open Modal"]
    [:div
     {:id "modals-here",
      :class "modal modal-blur fade",
      :style "display: none",
      :aria-hidden "false",
      :tabindex "-1"}
     [:div
      {:class "modal-dialog modal-lg modal-dialog-centered",
       :role "document"}
      [:div {:class "modal-content"}]]
     [:style
      "@import \"https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/5.2.2/css/bootstrap.min.css\";"]
     [:script
      {:src
       "https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js",
       ;; :integrity
       ;; "sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3",
       :crossorigin "anonymous"}]])))

(defn- modal [request]
  (util/hiccup-response
   [:div
    {:class "modal-dialog modal-dialog-centered"}
    [:div
     {:class "modal-content"}
     [:div
      {:class "modal-header"}
      [:h5 {:class "modal-title"} "Modal title"]]
     [:div {:class "modal-body"} [:p "Modal body text goes here."]]
     [:div
      {:class "modal-footer"}
      [:button
       {:type "button",
        :class "btn btn-secondary",
        :data-bs-dismiss "modal"}]]]]))

(defn- modal-post [request]
  ;(tap> [`modal-post "called"])
  (util/hiccup-response [:div "Called to server"]))

(def routes
  ["/example17"
   [""
    {:get {:handler main}
     :name ::main}]
   ["/modal"
    {:get {:handler modal}
     :post {:handler modal-post}
     :name ::modal}]])

(comment
  <script src= "https://cdn.jsdelivr.net/npm/bootstrap@5.2.2/dist/js/bootstrap.bundle.min.js" integrity= "sha384-OERcA2EqjJCMA+/3y+gxIOqMEjwtxJY7qPCqsdltbNJuaOe923+mo//f6V8Qbsw3" crossorigin= "anonymous" ></script>

  #_f)
