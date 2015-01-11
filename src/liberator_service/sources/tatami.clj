(ns liberator-service.sources.tatami
  (:require [net.cgrand.enlive-html :as html]
            [schema.core :as s]
            [liberator-service.models.db :as db]))

(def tatami-base-url "http://www.tatamifightwear.com/ProductDetails.asp?ProductCode=")
;the site adds the available sizes using some js. This regex will return the js called
(def tatami-regex-sizes-js #"TCN_addContent.*?;")
;regex to extract the sizes A{number}{0-2 letters}
(def tatami-regex-sizes #"A[0-9][A-Za-z]{0,2}")
(def tatami-regex-product-code #"(?<=ProductCode=).*")

(def tatami-bjj-gi-search "http://www.tatamifightwear.com/SearchResults.asp?searching=Y&sort=13&search=bjj+gi&show=100&page=1")

(def product-codes
  ["Black-Estilo-4.0"
   "White-Estilo-4.0"
   "Blue-Estilo-4.0"
   "Navy-Estilo-4.0"
   "zerogv3black"])

(defn get-product-codes
  []
  (db/get-product-codes-from-brand-name "tatami"))

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
      :content
      first))

(defn get-map
  [html-resource]
  (let [product-name (get-product-name html-resource)
        sizes (get-avail-sizes html-resource)]
    {:product_name product-name
     :sizes sizes}))

(defn get-map-from-product-code
  [product-code]
  (let [url (str tatami-base-url product-code)
        html-res (html/html-resource (java.net.URL. url))
        product-name (get-product-name html-res)
        sizes (get-avail-sizes html-res)]
    {:product_name product-name
     :sizes        sizes
     :product_code (str product-code)
     :url          (str url)}))


(defn update-db-product
  [tatami-item]
  (let [{:keys [product_code product_name sizes]} tatami-item]
    (db/update-product product_code product_name (clojure.string/join "," sizes))))

(defn update-db-product-from-code
  [product-code]
  (update-db-product (get-map-from-product-code product-code)))

(defn get-products-from-db
  []
  (db/get-products-from-brand-name "tatami"))

(def Tatami
  "The schema for a Tatami product"
  {:id s/Str
   :brand_id s/Str
   :product_code s/Str
   :product_name s/Str
   :sizes s/Str})

(defn extract-product-codes
  [url]
  (->> (html/select (html/html-resource (java.net.URL. url)) [:tr :> :td :> :a])
      (map #(:href (:attrs %)))
      (map #(re-seq tatami-regex-product-code %))))