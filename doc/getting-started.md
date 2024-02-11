# Setup

1. Required deps are specified in [deps.edn](../deps.edn)
2. [portal](https://github.com/djblue/portal) is such an awesome tool and a must if you are a Clojure developer in my opinion. Portal setup is in [user.clj](../dev/user.clj).
3. [server.clj](../src/dumrat/htmx_learn/server.clj) defines the server related functions along with hot reloading for the handler. I don't use ring `wrap-reload` because I tend to eval a changed function more than I save a file.
4. [system.clj](../src/dumrat/htmx_learn/system.clj) configures the system using integrant. start-system starts a server on port 3000.
