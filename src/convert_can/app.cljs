(ns convert-can.app
    (:require [reagent.core :as r]
              [stylefy.core :refer [use-style]]
              [re-frame.core :as rf]
              [convert-can.util :refer [format-price]]))

(def layout-style
  {:display "grid"
   :grid-template-columns "2fr 1fr"
   :grid-column-gap "2rem"
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
  {:font-size "24px"
   :font-weight "bold"
   :padding "0"
   :margin "0"})

(defn catalog-item [item]
  [:div (use-style item-card-style)
   [:img (use-style img-style {:src (:imageURL item)})]
   [:div
    [:div (:name item)]
    [:div (format-price (:price item))]
    (when-let [{:keys [amount totalPrice]} (:bulkPricing item)]
      [:div (str "or " amount " for " (format-price totalPrice))])]])

(defn catalog-list []
  [:div
   (map (fn [item]
          ^{:key (:id item)}
          [catalog-item item])
        @(rf/subscribe [:catalog]))])

(defn shopping-cart []
  [:div "lorem ipsum"])

(defn root []
  [:div (use-style layout-style)
   [catalog-list]
   [shopping-cart]])