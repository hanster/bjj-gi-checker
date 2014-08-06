(ns lobos.config
  (:use lobos.connectivity)
  (:require [liberator-service.models.schema :as schema]))


(let [db-host "localhost"
      db-port 5432
      db-name "bjjgi_dev"]

  (def pgdb {:classname "org.postgresql.Driver" ; must be in classpath
           :subprotocol "postgresql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
           :user "sam"
           :password "sampass"}))


(open-global schema/db-spec)


;; use this to look at the connection
;; (@global-connections :default-connection)
