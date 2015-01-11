(ns liberator-service.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [liberator-service.models.schema :as schema]
            [clj-time.core :as t]
            [clj-time.coerce :as tc]))

(defdb db schema/db-spec)

(defentity brands)
(defentity products)

(defn inital-products-setup []
  (insert products (values {:id 1 :brand_id 1 :product_code "estilo5.0navy"}))
  (insert products (values {:id 2 :brand_id 1 :product_code "estilo5.0blue"}))
  (insert products (values {:id 3 :brand_id 1 :product_code "Blue-Estilo-4.0"}))
  (insert products (values {:id 4 :brand_id 1 :product_code "Navy-Estilo-4.0"}))
  (insert products (values {:id 5 :brand_id 1 :product_code "zerogv3black"})))

(defn inital-setup []
  (insert brands (values {:id 1 :brand_name "tatami" :website_url "http://www.tatamifightwear.com" :product_url_ext "/ProductDetails.asp?ProductCode="}))
  (inital-products-setup))

;;(inital-products-setup)

(defn next-id
  []
  (+ 1 (count (select products))))

(defn insert-product-code
  [brand-id product-code]
  (insert products (values {:id (next-id) :brand_id brand-id :product_code product-code})))

(defn delete-product-by-id
  [product-id]
  (delete products (where {:id product-id})))

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

;; update products
(defn update-product [product-code product-name sizes]
  (update products
          (set-fields {:product_name product-name
                       :available_sizes sizes
                       :last_updated_date (java.sql.Timestamp. (.getTime (java.util.Date.)))})
          (where {:product_code product-code})))