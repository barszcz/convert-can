(ns convert-can.events
  (:require [re-frame.core :as rf])
  (:require-macros [convert-can.macros :refer [inline-file]]))

(def catalog
  "Load in the catalog of products as a data structure."
  (as-> (inline-file "public/products-data.json") $
    (.parse js/JSON $)
    (js->clj $ :keywordize-keys true)))

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:catalog catalog
     :cart {}}))
