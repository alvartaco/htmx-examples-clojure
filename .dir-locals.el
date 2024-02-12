((clojure-mode . ((cider-clojure-cli-aliases . ":repl/reloaded")
                  (cider-jack-in-nrepl-middlewares  .(cider.nrepl/cider-middleware portal.nrepl/wrap-portal)))))
