(ns filldaysn.time-utils
  (:require [clj-time.format :as f])
  (:gen-class
   :name filldays.time-utils))

(def ISOFormatter (f/formatters :date-time-no-ms))

(defn String->Date
  [str]
  (f/parse ISOFormatter str)
  )

(defn EnhancedDate->String
  [{date :date available :available}]
  {:date (f/unparse ISOFormatter date) :available available}
  )

(defn Strings->Dates
  "Converts a list of date-strings to dates"
  [s]
  (map String->Date s)
  )

(defn EnhancedDates->Strings
  "Converts a list of dates to string"
  [s]
  (map EnhancedDate->String s)
  )
