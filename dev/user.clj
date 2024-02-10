(ns user
  (:require [portal.api :as portal]))

(portal/open {:portal.colors/theme :portal.colors/gruvbox})
(add-tap #'portal.api/submit)

(tap> [:welcome!])
