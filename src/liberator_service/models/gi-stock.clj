(ns liberator-service.models.gi-stock
  (:require [liberator-service.sources.tatami :as tatami]
            [net.cgrand.enlive-html :as html][monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]))


(defn get-map-from-urls
  [url]
  (->> (java.net.URL. url)
       (html/html-resource)
       (tatami/get-map)))



(defn get-tatami-map
  []
  (map tatami/get-map-from-url tatami/get-all-urls))

;(defn in-db
;;   "Check subtitle already in db"
;;   [subtitle]
;;   (-> (let [url (:url subtitle)]
;;         (esd/search const/index-name
;;                     "subtitle"
;;                     :filter (q/term :url url)))
;;       :hits(get-tatami-map
;;       :total
;;       (> 0)))

(def gi-maps (get-tatami-map))

(get-tatami-map)
(println gi-maps)

(first tatami/get-all-urls)
(tatami/get-map-from-url (first tatami/get-all-urls))



;read in a html file from disk as a html-resource
(def test-blue-html-resource (html/html-snippet (slurp "resources/blue-estilio.html")))

(tatami/get-map test-blue-html-resource)

;; monger testing
(def conn (mg/connect))

(def db (mg/get-db conn "monger-test"))

(mc/insert-batch db "tatami" gi-maps)

(mc/find-maps db "tatami" {})
