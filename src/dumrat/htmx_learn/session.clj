(ns dumrat.htmx-learn.session)

(defonce ^:private sessions (atom {}))

(defn- create-session-and-get-id [session-id]
  (swap! sessions assoc session-id (atom nil))
  session-id)

(defn- get-session [session-id]
  (get @sessions session-id))

(defn get-or-create-session
  "If session-id nil, create session and return.
   If session-id isn't nil but it doesn't exist in sessions, create session and return
   Otherwise, return session"
  [session-id]
  (let [current-session (get-session session-id)
        new-session-id (if current-session session-id (create-session-and-get-id (str (random-uuid))))
        session (get-session new-session-id)]
    [new-session-id session]))

(comment


  @sessions

  (reset! sessions {})

  (get-or-create-session nil)
  (get-or-create-session (str (random-uuid)))
  (get-or-create-session "7d46f7bd-a82c-4d5a-85d1-7df10820f279")

  ,,)
