(ns liberator-service.test.sources.tatami
  (:use clojure.test
        liberator-service.sources.tatami))

(deftest test-tatami
  (testing "html resource scraping"
    (let [html-resource (net.cgrand.enlive-html/html-snippet (slurp "resources/blue-estilio.html"))]
      (is (= (get-product-name html-resource) "Blue Estilo 4.0 Premier BJJ GI"))
      (is (= (get-avail-sizes html-resource) '("A3" "A3L" "A4"))))))