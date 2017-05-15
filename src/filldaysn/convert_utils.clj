(ns filldaysn.convert-utils
  (:require [clj-time.format :as f])
  (:gen-class))

(def ISOFormatter (f/formatters :date-time-no-ms))

(defn string-date
  [str]
  (f/parse ISOFormatter str))

(defn enhancedDate-string
  [{date :date available :available}]
  {:date (f/unparse ISOFormatter date) :available available})

(defn strings-dates
  "Converts a list of date-strings to dates"
  [str]
  (map string-date str))

(defn enhancedDates-strings
  "Converts a list of dates to string"
  [enhanced-dates]
  (map enhancedDate-string enhanced-dates))

(defn string-number [str]
  (try
    (let [n (read-string str)]
      (if (number? n) n nil))
    (catch Exception e nil)))
