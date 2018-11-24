(ns convert-can.events
  (:require [re-frame.core :as rf])
  (:require-macros [convert-can.macros :refer [inline-file]]))

(def catalog
  "Load in the catalog of products as a data structure."
  (as-> (inline-file "public/products-data.json") $
    (.parse js/JSON $)
    (js->clj $ :keywordize-keys true) ;; @todo kebab-case keys
    (:treats $)
    (map (fn [item] [(:id item) item]) $)
    (into (sorted-map) $))) ;; sorted-map instead of array for easier/faster lookup

(rf/reg-event-db
 :initialize
 (fn [_ _]
   {:catalog catalog
    :cart (array-map)})) ;; array-map to preserve insertion order

(rf/reg-event-db
 :add-item
 (fn [db [_ id]]
   (update-in db [:cart id] inc)))

(rf/reg-event-db
 :clear-cart
 (fn [db _]
   (assoc db :cart (array-map))))