(ns user
  (:require [portal.api :as portal]
            [clojure.datafy :as d]))

(portal/start {:portal.colors/theme :portal.colors/gruvbox
               :port 45000
               :host "localhost"})

(defn error->data [ex]
  (assoc (d/datafy ex) :runtime :clj))

(defn submit [value]
  (println "submitting value")
  (portal/submit
   (if-not (instance? Exception value)
     value
     (error->data value))))

(add-tap #'submit)

(tap> [:welcome!])

(comment

  (+ 1 2)

  (tap> (Exception. "e")))
