(ns convert-can.subs
  (:require [re-frame.core :as rf]))

(defn- compute-bulk-price [qty bulk-qty bulk-price]
  (* (quot qty bulk-qty) bulk-price))

(defn- compute-individual-price [qty price]
  (* qty price))

(defn- compute-price [qty price bulk-pricing]
  (if-let
   [{:keys [amount totalPrice]} bulk-pricing]
    (let [bulk-price (compute-bulk-price qty amount totalPrice)
          remainder (rem qty amount)
          individual-price (compute-individual-price remainder price)]
      (+ bulk-price individual-price))
    (compute-individual-price qty price)))

(defn- compute-item-price [qty item]
  (compute-price qty (:price item) (:bulkPricing item)))

(rf/reg-sub
 :catalog
 (fn [db _]
   (:catalog db)))

(rf/reg-sub
 :cart
 (fn [db _]
   (:cart db)))

(rf/reg-sub
 :items-in-cart ;; @todo clearer name for this
 :<- [:catalog]
 :<- [:cart]
 (fn [[catalog cart] _]
   (map (fn [[id qty]] (let [item (catalog id)]
                         {:item item
                          :qty qty
                          :price (compute-item-price qty item)})) cart)))

(rf/reg-sub
 :total-price
 :<- [:items-in-cart]
 (fn [items _]
   (reduce + (map :price items))))