(ns liberator-service.sources.tatami
  (:require [net.cgrand.enlive-html :as html]))

(def tatami-base-url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=")
;the site adds the available sizes using some js. This regex will return the js called
(def tatami-regex-sizes-js #"TCN_addContent.*?;")
;regex to extract the sizes A{number}{0-2 letters}
(def tatami-regex-sizes #"A[0-9][A-Za-z]{0,2}")
(def product-codes
  ["Black-Estilo-4.0"
   "White-Estilo-4.0"
   "Blue-Estilo-4.0"
   "Navy-Estilo-4.0"
   "zerogv3black"])

(def get-all-urls
  (map #(str tatami-base-url %) product-codes))

(defn get-avail-sizes
  "get the available sizes for from a tatami webpage "
  [html-resource]
  (->>
    (clojure.string/join html-resource)
    (re-seq tatami-regex-sizes-js)
    (mapcat #(re-seq tatami-regex-sizes %))))

(defn get-product-name
  "get the product name from the html resource"
  [html-resource]
  (-> (html/select html-resource #{[:font.productnamecolorLARGE :span]})
      first
      :content))

(defn get-map
  [html-resource]
  {:product_name
    (get-product-name html-resource)
    :sizes
    (get-avail-sizes html-resource)})

