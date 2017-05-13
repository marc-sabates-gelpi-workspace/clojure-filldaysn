(ns filldaysn.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [filldaysn.time-utils :refer :all])
  (:gen-class
   :name filldaysn.core))

(defn available
  [date]
  {:date date :available true}
  )

(defn unavailable
  [date]
  {:date date :available false})

(defn nextDayL
  [day [current & following :as all]]
  (let [nextDay (->> (t/days 1)
                     (t/plus day))]
    (cond
     (and (not (nil? current)) (= day current)) (lazy-seq
                        (cons (available day)
                              (nextDayL nextDay following)))
     :else (lazy-seq
            (cons (unavailable day)
                  (nextDayL nextDay all)))
     )
    )
  )

(defn nextDay
  [[current & _ :as all]]
  (nextDayL current all)
  )

(defn String->Number [str]
  (let [n (read-string str)]
       (if (number? n) n nil)))

(defn -main
  [n & args]
  (let [num (String->Number n)]
    (->> args
        Strings->Dates
        sort
        nextDay
        (take num)
        EnhancedDates->Strings
        prn))
)
