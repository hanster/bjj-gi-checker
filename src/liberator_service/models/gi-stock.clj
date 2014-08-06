(ns liberator-service.models.gi-stock
  (:require [liberator-service.sources.tatami :as tatami]
            [net.cgrand.enlive-html :as html]))


(defn get-map-from-urls
  [url]
  (->> (java.net.URL. url)
       (html/html-resource)
       (tatami/get-map)))



(defn get-tatami-map
  []
  (map tatami/get-map-from-url tatami/get-all-urls))

(def gi-maps (get-tatami-map))

(get-tatami-map)
(println gi-maps)

(first tatami/get-all-urls)
(tatami/get-map-from-url (first tatami/get-all-urls))



;read in a html file from disk as a html-resource
(def test-blue-html-resource (html/html-snippet (slurp "resources/blue-estilio.html")))

(tatami/get-map test-blue-html-resource)
