(ns convert-can.app
    (:require [reagent.core :as r]
              [stylefy.core :refer [use-style]]))

(def img-style
  {:margin-top "20px"})

;; Dog GIF react component

(defn dog-image []
  [:img (use-style img-style
          {:src "https://i.imgur.com/YCnyVDq.gif"})])

(def header-style
  {:font-size "24px"
   :font-weight "bold"
   :padding "0"
   :margin "0"})

;; Main react component

(defn root []
  [:div
    [:h1 (use-style header-style)
     "Hello!"]])
    ; [dog-image]])


(println "lol hi")