(ns convert-can.subs
  (:require [re-frame.core :as rf]))

(defn- compute-bulk-price [qty bulk-qty bulk-price]
  "Computes price just for the part of qty evenly divisible by the bulk-qty.
   This means the caller has to compute the remainder itself and pass that to
   compute-individual-price."
  (* (quot qty bulk-qty) bulk-price))

(defn- compute-individual-price [qty price]
  "Was this necessary? Probably not but makes things slightly clearer."
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
  "Wrapper letting us pass in a catalog item data structure to the price function."
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
   "This one's inefficient because it'll recompute all the prices whenever
    one item's quantity changes. We're dealing with a small enough
    dataset that this is fine but it would be low-hanging fruit for optimization."
   (map (fn [[id qty]] (let [item (catalog id)]
                         {:item item
                          :qty qty
                          :price (compute-item-price qty item)})) cart)))

(rf/reg-sub
 :total-price
 :<- [:items-in-cart]
 (fn [items _]
   "Straightforward map-and-sum. Could be made more efficient with a transducer."
   (reduce + (map :price items))))