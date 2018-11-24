(ns convert-can.macros)

(defmacro inline-file [path]
  "Simple utility to read in a static file as a string at build time."
  (slurp path))
