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
