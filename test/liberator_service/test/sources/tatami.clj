(ns liberator-service.test.sources.tatami
  (:use clojure.test
        liberator-service.sources.tatami)
  (:require [net.cgrand.enlive-html :as html]))

(deftest test-tatami
  (testing "html resource scraping"
    (let [html-resource (html/html-snippet (slurp "resources/blue-estilio.html"))]
      (is (= (get-product-name html-resource) "Blue Estilo 4.0 Premier BJJ GI"))
      (is (= (get-avail-sizes html-resource) '("A3" "A3L" "A4"))))))

(deftest test-female-size
  (testing "html resource scraping the female size"
    (let [html-resource (html/html-snippet (slurp "resources/ladiesEstilio5White.html"))]
      (is (= (get-avail-sizes-regex-style html-resource) '("F1" "F3" "F4"))))))

(deftest test-option-style-sizes
  (testing "html resource scraping sizes when the select options are already on the page"
    (let [html-resource (html/html-snippet (slurp "resources/fudoshin.html"))]
      (is (= (get-avail-sizes-option-style html-resource) '("A1" "A2" "A3" "A4" "A5"))))))


(deftest test-option-style-sizes-when-regex-is-empty
  (testing "html resource scraping sizes when the select options are already on the page"
    (let [html-resource (html/html-snippet (slurp "resources/fudoshin.html"))]
      (is (= (get-avail-sizes html-resource) '("A1" "A2" "A3" "A4" "A5"))))))