(ns dumrat.htmx-learn.session)

(defonce ^:private sessions (atom {}))

(defn- create-session-and-get-id []
  (let [session-id (str (random-uuid))]
    (swap! sessions assoc session-id (atom nil))
    session-id))

(defn- get-session [session-id]
  (get @sessions session-id))

(defn get-or-create-session [session-id]
  (let [session-id (or session-id (create-session-and-get-id))
        session (get-session session-id)]
    [session-id session]))

(comment

  @sessions
  (str (random-uuid))

  ,,)
