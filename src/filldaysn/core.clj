(ns filldaysn.core
  (:require [clj-time.core :as t]
            [filldaysn.convert-utils :as conv]
            [clojure.spec.alpha :as s])
  (:gen-class))

(def one-day-adder (t/days 1))

(defn- available
  [date]
  {:date date :available true})

(defn- unavailable
  [date]
  {:date date :available false})

(defn- next-day-lazy
  [day [firstDay & followingDays :as allDays]]
  (let [nextDay (->> one-day-adder
                     (t/plus day))]
    (if (and (not (nil? firstDay))
             (= day firstDay))
      (lazy-seq
       (cons (available day)
             (next-day-lazy nextDay followingDays)))
      (lazy-seq
       (cons (unavailable day)
             (next-day-lazy nextDay allDays))))))

(s/def ::dates-seq (s/coll-of #(instance? org.joda.time.DateTime %) :distinct true))
(s/def ::enhanced-date (s/keys :req-un [::date ::available]))
(s/def ::enhanced-dates-seq (s/every ::enhanced-date))

(defn next-day
  [[firstDay & _ :as all]]
  {:pre [(s/valid? ::dates-seq all)]
   :post [(s/valid? ::enhanced-dates-seq %)]}
  (if (nil? firstDay)
    (empty all)
    (next-day-lazy firstDay all)))

(defn -main
  [n & dates]
  (let [num (conv/string-number n)]
    (if (nil? num)
      (prn "The first parameter needs to be a number!")
      (->> dates
          conv/strings-dates
          sort
          next-day
          (take num)
          conv/enhancedDates-strings
          prn))))
