# HTMX examples reworked in Clojure

This is a learning exercise for myself. I'm going to go through the official htmx [examples](https://htmx.org/examples/) and implement them using Clojure.

htmx niceties for Clojure are present in [SimpleUI](https://github.com/whamtet/simpleui) but I'm going to skip this to keep things simpler.

### How to use

[In action here](https://www.dumrat.com/htmx-examples/index.html)

1. Try out at repl. Else, run `clj -M -m dumrat.htmx-learn.core` at root folder.
2. Point the browser to [htmx-examples](http://localhost:3000/htmx-examples/index.html).

### libs used

1. [integrant](https://github.com/weavejester/integrant) for system creation.
2. [reitit](https://github.com/metosin/reitit) for routing and ring handler
3. [ring-jetty-adapter](https://github.com/ring-clojure/ring) for jetty server
4. [hiccup](https://github.com/weavejester/hiccup) for html generation
