(ns convert-can.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [stylefy.core :as stylefy]
            [convert-can.views :as views]
            [convert-can.events :as events]
            [convert-can.subs :as subs]))

;; Global styles
(def body-style
  {:margin "20px"
   :background-color "#E9EAEC"
   :font-family "'Lato', sans-serif"})

(defn start []
  (stylefy/init)
  (stylefy/tag "body" body-style)
  (rf/dispatch-sync [:initialize])
  (r/render [views/root]
            (.getElementById js/document "root")))

(start)
