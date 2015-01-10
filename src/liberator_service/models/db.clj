(ns liberator-service.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [liberator-service.models.schema :as schema]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]))


(defdb db schema/db-spec)

(defentity brands)
(defentity products)

(defn inital-setup []
  (insert brands (values {:id 1 :brand_name "tatami" :website_url "http://www.tatamifightwear.com" :product_url_ext "/ProductDetails.asp?ProductCode="}))
  (insert products (values {:id 1 :brand_id 1 :product_code "Black-Estilo-4.0"}))
  (insert products (values {:id 2 :brand_id 1 :product_code "White-Estilo-4.0"}))
  (insert products (values {:id 3 :brand_id 1 :product_code "Blue-Estilo-4.0"}))
  (insert products (values {:id 4 :brand_idkekj 1 :product_code "Navy-Estilo-4.0"}))
  (insert products (values {:id 5 :brand_id 1 :product_code "zerogv3black"})))


(defn inital-products-setup []
  (insert products (values {:id 1 :brand_id 1 :product_code "estilo5.0navy"}))
  (insert products (values {:id 2 :brand_id 1 :product_code "White-Estilo-4.0"}))
  (insert products (values {:id 3 :brand_id 1 :product_code "Blue-Estilo-4.0"}))
  (insert products (values {:id 4 :brand_id 1 :product_code "Navy-Estilo-4.0"}))
  (insert products (values {:id 5 :brand_id 1 :product_code "zerogv3black"})))

;;(inital-products-setup)

(select products
        (fields :product_code))

(defn- get-brand-id-from-brand-name
  [brand]
  (:id (first (select brands
                      (fields :id)
                      (where {:brand_name brand})))))

(defn get-product-codes-from-brand-id
  [id]
  (let [product-code-map (select products
                                 (fields :product_code)
                                 (where {:brand_id id}))]
    (map #(:product_code %) product-code-map)))

(defn get-product-codes-from-brand-name
  [brand]
  (let [brand_id (get-brand-id-from-brand-name brand)]
    (println brand_id)
    (get-product-codes-from-brand-id brand_id)))

(defn get-products-from-brand-name
  [brand]
  (let [brand_id (get-brand-id-from-brand-name brand)]
    (select products
            (where {:brand_id brand_id}))))

(get-product-codes-from-brand-id 1)

(get-product-codes-from-brand-name "tatami")

(select brands
                         (fields :id)
                         (where {:brand_name "tatami"}))


;; update products
(defn update-product [product-code product-name sizes]
  (update products
          (set-fields {:product_name product-name
                       :available_sizes sizes
                       :last_updated_date (java.sql.Timestamp. (.getTime (java.util.Date.)))})
          (where {:product_code product-code})))
;
;(def product-codes-test (get-product-codes-from-brand-name "tatami"))
;
;(map #(:product_code %) product-codes-test)
