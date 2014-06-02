(ns liberator-service.routes.home
  (:require [compojure.core :refer :all]
            [liberator.core
             :refer [defresource resource request-method-in]]
            [cheshire.core :refer [generate-string]]
            [noir.io :as io]
            [clojure.java.io :refer [file]]
            [liberator-service.models.tatami :as tatami]))

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
(defresource get-gi-stock
  :allowed-methods [:get]
  :handle-ok (fn [_] (generate-string (tatami/get-all)))
  :available-media-types ["application/json"])


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
  (ANY "/get-gi" request get-gi-stock))

