(ns convert-can.app
  (:require [reagent.core :as r]
            [stylefy.core :refer [use-style]]
            [re-frame.core :as rf]
            [convert-can.util :refer [format-price]])
  (:require-macros [convert-can.macros :refer [dispatch]]))

(def layout-style
  {:display "grid"
   :grid-template-columns "2fr 1fr"
   :grid-column-gap "30%"
   :grid-row-gap "20px"
   :margin "auto"
   :width "90%"})

(def item-card-style
  {:display "grid"
   :align-items "center"
   :grid-template-columns "1fr 2fr"
   :grid-column-gap "10px"
   :border "2px solid #aaa"
   :margin-bottom "10px"})

(def img-style
  {:max-width "100px"
  :max-height "100px"})

(def header-style
  {:grid-column "1 / 3"
   :text-align "center"
   :font-size "24px"
   :font-weight "bold"
   :padding "0"
   :margin "0"})

(def cart-style
  {:border "2px solid #aaa"
   :padding "10px"
   :display "flex"
   :flex-direction "column"
   :justify-content "space-between"
   })

(def button-group-style
  {:margin-left "10px"})

(def cart-subhead-style
  {:margin "0 auto"})

(def cart-summary-style
  {:align-self "center"
   :text-align "center"})

(def cart-item-style
  {:margin-bottom "10px"})

(defn catalog-item [item]
  [:div (use-style item-card-style)
   [:img (use-style img-style {:src (:imageURL item)})]
   [:div
    [:div [:strong (:name item)]]
    [:div (format-price (:price item))]
    (when-let [{:keys [amount totalPrice]} (:bulkPricing item)]
      [:div (str "(or " amount " for " (format-price totalPrice) ")")])
    [:button
    ;  {:on-click #(rf/dispatch [:add-item (:id item)])}
     {:on-click (dispatch :add-item (:id item))}
     "Add to cart"]]])

(defn catalog-list []
  [:div
   (map (fn [[id item]]
          ^{:key id}
          [catalog-item item])
        @(rf/subscribe [:catalog]))])

(defn cart-line [{:keys [item qty price]}]
  [:div (use-style cart-item-style)
   [:div
    [:strong (:name item)] " x " [:strong qty]]
   [:div
    "Price: " [:strong (format-price price)]
    [:span (use-style button-group-style)
     [:button
      {:on-click (dispatch :add-item (:id item))} "+"]
     [:button
      {:on-click (dispatch :decrement-item (:id item))
       :disabled (= qty 1)} "-"]
     [:button
      {:on-click (dispatch :remove-item (:id item))} "Remove"]]]])

(defn cart-summary []
  [:div (use-style cart-summary-style)
   [:div [:strong "Total: " (format-price @(rf/subscribe [:total-price]))]]
   [:button {:on-click (dispatch :clear-cart)} "Clear"]])

(defn cart-items []
  [:div
   (map (fn [{item :item :as item-with-qty}]
          ^{:key (:id item)}
          [cart-line item-with-qty])
        @(rf/subscribe [:items-in-cart]))])

(defn shopping-cart []
  [:div
   [:div (use-style cart-style)
    [:h2 (use-style cart-subhead-style) "Cart"]
    [cart-items]
    [cart-summary]]])

(defn root []
  [:div (use-style layout-style)
   [:h1 (use-style header-style) "Bakery"]
  [catalog-list]
  [shopping-cart]])