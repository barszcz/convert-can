(ns convert-can.util)

(defn format-price [n]
  (str "$" (.toFixed n 2)))

(format-price 4)