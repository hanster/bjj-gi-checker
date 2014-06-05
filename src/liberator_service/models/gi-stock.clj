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

;read in a html file from disk as a html-resource
(def test-blue-html-resource (html/html-snippet (slurp "resources/blue-estilio.html")))

(tatami/get-map test-blue-html-resource)
