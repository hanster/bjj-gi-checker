(ns lobos.migrations
  (:refer-clojure :exclude
                  [alter drop bigint boolean char double float time])
  (:use (lobos [migration :only [defmigration]] core schema config)))

(defmigration add-brand-table
  (up [] (create
          (table :brands
                 (integer :id :primary-key)
                 (varchar :brand_name 30)
                 (varchar :website_url 100)
                 (varchar :product_url_ext 100))))
  (down [] (drop
            (table :brands))))


(defmigration add-product-table
  (up [] (create
          (table :products
                 (integer :id :primary-key)
                 (integer :brand_id [:refer :brands :id] :not-null)
                 (varchar :product_code 50)
                 (varchar :product_name 50)
                 (varchar :available_sizes 50)
                 (date :last_updated_date))))
  (down [] (drop
            (table :products))))
