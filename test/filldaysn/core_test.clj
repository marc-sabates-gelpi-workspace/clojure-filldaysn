(ns filldaysn.core-test
  (:require [clojure.test :refer :all]
            [filldaysn.core :refer [next-day]]
            [filldaysn.convert-utils :as conv]
            [clj-time.core :as t]
            [clojure.spec.test.alpha :as stest]))

(testing "next-day"
  (deftest should-return-empty-list-when-empty-parameter
    (testing "should return empty list when empty parameter"
      (is (empty? (next-day '())))))

  (deftest should-return-available-date
    (testing "should return avaiable date"
      (is
       (=
        (list {:date "2017-05-17T00:00:00Z", :available true})
        (conv/enhancedDates-strings
         (take 1
               (next-day
                (list
                 (conv/string-date "2017-5-17T00:00:00Z")))))))))

  (deftest should-return-auauua-dates
    (testing "should return avail+unavail+avail+unavail+unavail+avail dates"
      (is
       (=
        (list
         {:date "2017-05-17T00:00:00Z", :available true}
         {:date "2017-05-18T00:00:00Z", :available false}
         {:date "2017-05-19T00:00:00Z", :available true}
         {:date "2017-05-20T00:00:00Z", :available false}
         {:date "2017-05-21T00:00:00Z", :available false}
         {:date "2017-05-22T00:00:00Z", :available true})
        (conv/enhancedDates-strings
         (take 6
               (next-day
                (list
                 (conv/string-date "2017-05-17T00:00:00Z")
                 (conv/string-date "2017-05-19T00:00:00Z")
                 (conv/string-date "2017-05-22T00:00:00Z")))))))))

    (deftest should-test-next-day
      (testing "should test next day"
        (is (let [result (stest/summarize-results (stest/check `next-day))
                  total (:total result)
                  passed (:check-passed result)]
              (and (= total passed)
                   (not (zero? total))))))))
