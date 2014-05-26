(ns liberator-service.models.tatami
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all])
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


(defn get-avail-sizes
  "get the available sizes for from a tatami webpage "
  [html-source]
  (map #(re-seq tatami-regex-sizes %)
       (apply list (re-seq
                    tatami-regex-sizes-js
                    html-source))))

(def conn (mg/connect))

(def db (mg/get-db conn "monger-test"))

(mc/insert db "documents" { :_id (ObjectId.) :first_name "John" :last_name {"Lennon" "test"} })

(mc/find-one db "documents" {})
(mc/remove db "tatami" {})

;batch insert. Sizes is an array
(mc/insert-batch db "tatami" [{ :_id "estilio-blue-4.0"
                                :product_name "Tatami Estilio Blue 4.0"
                                :proudct_url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=Blue-Estilo-4.0"
                                :sizes ["A0" "A1"]}
                              { :_id "estilio-white-4.0"
                                :product_name "Tatami Estilio White 4.0"
                                :proudct_url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=White-Estilo-4.0"
                                :sizes ["A0" "A1"]}])

;use $set so that only that attribute gets updated and the others are left alone
(mc/update db "tatami" {:_id "estilio-blue-4.0"} {$set {:sizes ["A1"]}})

;returns a single map
(mc/find-one-as-map db "tatami" {:_id "estilio-blue-4.0"})
((mc/find-one-as-map db "tatami" {:product_name "Tatami Estilio Blue 4.0"}) :sizes)
(mc/find-maps db "tatami" {:product_name "Tatami Estilio Blue 4.0"})

(count (mc/find-maps db "tatami" {}))
