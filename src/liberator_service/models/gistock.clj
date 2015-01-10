(ns liberator-service.models.gistock
  (:require [liberator-service.sources.tatami :as tatami]
            [net.cgrand.enlive-html :as html]))

(defn get-all-tatami
  []
  (map tatami/get-map-from-product-code tatami/product-codes))

(defn get-all-tatami-from-db
  []
  (tatami/get-products-from-db))

(defn update-all-tatami
  "update all the tatami products in the database from the website"
  []
  (pmap tatami/update-db-product-from-code (tatami/get-product-codes)))



;(get-tatami-map)
;(println gi-maps)

;(first tatami/get-all-urls)
;(tatami/get-map-from-url (first tatami/get-all-urls))



;read in a html file from disk as a html-resource
;(def test-blue-html-resource (html/html-snippet (slurp "resources/blue-estilio.html")))

;(tatami/get-map test-blue-html-resource)
