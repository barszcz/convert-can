(ns convert-can.macros
    (:require [re-frame.core :as rf]))

(defmacro inline-file [path]
  "Simple utility to read in a static file as a string at build time."
  (slurp path))

(defmacro dispatch [& args]
  "Allow us to dispatch slightly more ergonomically."
  `(fn []
     (rf/dispatch [~@args])))