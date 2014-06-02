(ns liberator-service.models.gi-stock
  (:require [liberator-service.sources.tatami :as tatami]
            [net.cgrand.enlive-html :as html][monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]))



(defn get-map-from-url
  [url]
  (->> (java.net.URL. url)
       (html/html-resource)
       (tatami/get-map)))


(defn get-tatami-map
  []
  (map get-map-from-url tatami/get-all-urls))
