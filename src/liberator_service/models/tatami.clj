(ns liberator-service.models.tatami
  (:require [net.cgrand.enlive-html :as html]))

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

;read in a html file from disk as a html-resource
(def test-blue-html-resource (html/html-snippet blue-html))

(html/select blue-html-resource #{[:font.productnamecolorLARGE :span]})
(html/text (html/select blue-html-resource #{[:font.productnamecolorLARGE :span]}))
(-> (html/select test-blue-html-resource #{[:font.productnamecolorLARGE :span]}) first :content)

(get-avail-sizes blue-html)

(defn update-gi
  [gi-map]
  (->> (fetch-url (gi-map :product_url))
       (get-avail-sizes)))


(def test-list '("A0" "A2"))

