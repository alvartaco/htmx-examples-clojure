(ns user
  (:require [portal.api :as portal]))

(portal/start {:portal.colors/theme :portal.colors/gruvbox
               :port 45000
               :host "localhost"})
(add-tap #'portal.api/submit)

(tap> [:welcome!])
