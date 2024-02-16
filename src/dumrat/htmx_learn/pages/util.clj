(ns dumrat.htmx-learn.pages.util
  (:require [reitit.core :refer [match-by-name match->path] :as r]
            [ring.util.response :as resp]))

(defn header []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   [:meta {:name "description" :content "This is an implementation for htmx official example 1 - Click to Edit with a Clojure back-end."}]
   [:title "</> htmx ~ Examples"]
   [:link {:rel "stylesheet" :href "/assets/css/site.css"}]
   [:script {:src "https://unpkg.com/htmx.org@1.9.10"}]
   [:script {:src "https://cdn.jsdelivr.net/npm/sweetalert2@11"}]])

;; TODO: 1. Remove hard-coded back-link by adding request to params.
;; TODO: 2. Hide back-link on main page.
(defn wrap-page
  "If request doesn't have htmx headers, wrap in page. Otherwise returns as-is"
  [request body]
  (if-not (get-in request [:headers "hx-request"])
    [:html {:lang "en"}
     (header)
     [:body {:hx-boost true :hx-ext "class-tools, preload"}
      [:center [:a {:href "/htmx-examples/index.html"} "\u2190 Back to main page"]]
      [:div {:class "c content"}
       body]]]
    body))

(defn name->path [{::r/keys [router]} name & {:keys [path-params query-params]}]
  (-> router
      (match-by-name name path-params)
      (match->path query-params)))

(defn hiccup-response [body]
  (-> body
      resp/response
      (resp/header "Content-Type" "hiccup")))

(defn wrap-page-hiccup [request body]
  (hiccup-response (wrap-page request body)))

(defn get-state-or-init [path init-val]
  (fn [request]
    (let [state (get request :session)]
      (when-not (get @state path)
        (swap! state assoc path (atom init-val)))
      (get @state path))))

(comment

  ;;TODO: Write a macro that can convert the first case into second:

  ;; 1. (defn some-fn [arg1] (tap> {:in `some-fn :arg1 arg1}))
  ;; 2. (defn some-fn [arg1] (util/tap> 'arg1))

  ;;Forget it. Seems this is too hard to do. See here: https://groups.google.com/g/clojure/c/Zpc2yaZDxqA?pli=1

  (require '[clojure.repl :refer [apropos]])
  (apply require clojure.main/repl-requires)

  #_f)
