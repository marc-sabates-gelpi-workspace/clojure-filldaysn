(ns filldaysn.core
  (:require [clj-time.core :as t]
            [filldaysn.convert-utils :as conv])
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

(defn next-day
  [[firstDay & _ :as all]]
  (if (nil? firstDay)
    '()
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
