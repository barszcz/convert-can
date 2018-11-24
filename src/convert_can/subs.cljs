(ns convert-can.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :catalog
 (fn [db _]
   (:catalog db)))