(ns liberator-service.routes.home
  (:require [compojure.core :refer :all]
            [liberator.core
             :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]
            [noir.io :as io]
            [clojure.java.io :refer [file]]
            [liberator-service.models.gistock :as gistock]
            [liberator.representation :as rep]))

(def users (atom ["John" "Jane" "tom"]))


(defresource get-users
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string @users))
  :available-media-types ["application/json"])

(defresource add-user
  :method-allowed? (request-method-in :post)
  :malformed?
  (fn [context]
    (let [params (get-in context [:request :form-params])]
      (empty? (get params "user"))))
  :handle-malformed "user name cannot be empty!"
  :post!
  (fn [context]
    (let [params (get-in context [:request :form-params])]
      (swap! users conj (get params "user"))))
  :handle-created (fn [_] (generate-string @users))
  :available-media-types["application/json"]
  )

;unfinished
(defresource get-gistock
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string (gistock/get-all-tatami)))
  :available-media-types ["application/json"])

(defresource get-gistock-from-db
             :allowed-methods [:get]
             :handle-ok (fn [_] (generate-string (gistock/get-all-tatami-from-db)))
             :available-media-types ["application/json"])

(defresource update-gistock
   :allowed-methods [:get]
   :handle-ok (fn [_] (generate-string(gistock/update-all-tatami)))
   :available-media-types ["text/html"])

(defresource get-gistock-html-table
             :allowed-methods [:get]
             :handle-ok (rep/as-response (gistock/get-all-tatami-from-db) {:representation {:media-type "text/html" :charset "UTF-8"}})
             :available-media-types ["text/html"])

(defresource home
  :available-media-types ["text/html"]

  :exists?
  (fn [context]
    [(io/get-resource "/home.html")]
    {::file (file (str (io/resource-path) "/home.html"))})

  :handle-ok
  (fn [{{{resource :resource} :route-params} :request}]
    (clojure.java.io/input-stream (io/get-resource "/home.html")))

  :last-modified
  (fn [{{{resource :resource} :route-params} :request}]
    (.lastModified (file (str (io/resource-path) "/home.html")))))


(defroutes home-routes
  (ANY "/" request home)
  (ANY "/add-user" request add-user)
  (ANY "/users" request get-users)
  (ANY "/get-gi" request get-gistock)
  (ANY "/get-gi-from-db" request get-gistock-from-db)
  (ANY "/get-gi-table" request get-gistock-html-table)
  (ANY "/update-gi" request update-gistock))

