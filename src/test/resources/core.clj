(ns core
  (:require [clojure.test :refer :all]
  [clojure-sonar-example.core :refer :all]))

;This line has only comments
(deftest a-test
  (testing "FIXME, I fail."; This line has comments and tokens
    (is (= 0 1))))
