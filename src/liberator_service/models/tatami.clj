(ns liberator-service.models.tatami
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [net.cgrand.enlive-html :as html])
  (:import org.bson.types.ObjectId))

;web address for tatami gi
(def tatami-base-url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=")
(def tatami-estilio-black (str tatami-base-url "Black-Estilo-4.0"))
(def tatami-estilio-white "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=White-Estilo-4.0")
(def tatami-estilio-blue "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=Blue-Estilo-4.0")
(def tatami-estilio-navy "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=Navy-Estilo-4.0")
(def zerogv3-black (str tatami-base-url "zerogv3black"))
;the site adds the available sizes using some js. This regex will return the js called
(def tatami-regex-sizes-js #"TCN_addContent.*?;")
;regex to extract the sizes A{number}{0-2 letters}
(def tatami-regex-sizes #"A[0-9][A-Za-z]{0,2}")

(defn fetch-url
  "fetch the url and return it as a string"
  [url]
  (apply str (html/html-resource (java.net.URL. url))))



(defn get-avail-sizes
  "get the available sizes for from a tatami webpage "
  [html-source]
  (->> (re-seq tatami-regex-sizes-js html-source)
       (map #(re-seq tatami-regex-sizes %))
       (apply concat)))


(def blue-html (slurp "resources/blue-estilio.html"))
(def blue-html-resource (html/html-resource (java.net.URL. tatami-estilio-blue)))


(html/select blue-html-resource #{[:font.productnamecolorLARGE :span]})
(html/text (html/select blue-html-resource #{[:font.productnamecolorLARGE :span]}))
(-> (html/select blue-html-resource #{[:font.productnamecolorLARGE :span]}) first :content)

(get-avail-sizes blue-html)

(defn update-gi
  [gi-map]
  (->> (fetch-url (gi-map :product_url))
       (get-avail-sizes)))



(def conn (mg/connect))

(def db (mg/get-db conn "monger-test"))


(mc/find-one db "documents" {})
(mc/remove db "tatami" {})

;batch insert. Sizes is an array
(mc/insert-batch db "tatami" [{ :_id "Blue-Estilo-4.0"
                                :product_name "Tatami Estilio Blue 4.0"
                                :product_url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=Blue-Estilo-4.0"
                                :sizes ["A0" "A1"]}
                              { :_id "estilio-white-4.0"
                                :product_name "Tatami Estilio White 4.0"
                                :product_url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=White-Estilo-4.0"
                                :sizes ["A0" "A1"]}
                              { :_id "estilio-navy-4.0"
                                :product_name "Tatami Estilio Navy 4.0"
                                :product_url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=Navy-Estilo-4.0"
                                :sizes ["A0" "A1"]}
                              ])

(def test-list '("A0" "A2"))
;use $set so that only that attribute gets updated and the others are left alone
;(def test-gi-map (update-gi (mc/find-one-as-map db "tatami" {:_id "estilio-blue-4.0"})))
;(update-gi (mc/find-one-as-map db "tatami" {:_id "estilio-blue-4.0"}))


;(mc/update db "tatami" {:_id "estilio-blue-4.0"} {$set {:sizes test-tatami-get-sizes }})

;returns a single map
(mc/find-one-as-map db "tatami" {:_id "estilio-blue-4.0"})
(type ((mc/find-one-as-map db "tatami" {:product_name "Tatami Estilio Blue 4.0"}) :sizes))
(defn get-all
  []
  (mc/find-maps db "tatami" {}))

(get-all)

(count (mc/find-maps db "tatami" {}))
