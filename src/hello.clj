(ns hello)

(defn greet []
  (println "Hello world!"))

(defn run [opts]
  (greet))

(comment
  (run {})
  ,,)
