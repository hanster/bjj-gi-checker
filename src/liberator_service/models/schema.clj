(ns liberator-service.models.schema
  (:use [lobos.core :only (defcommand migrate)])
  (:require [lobos.migration :as lm]))

(let [db-host "localhost"
      db-port 5432
      db-name "bjjgi_dev"]

  (def db-spec {:classname "org.postgresql.Driver" ; must be in classpath
           :subprotocol "postgresql"
           :subname (str "//" db-host ":" db-port "/" db-name)
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
           :user "sam"
           :password "sampass"}))

(defcommand pending-migrations []
  (lm/pending-migrations db-spec sname))

(pending-migrations)


 (migrate)

 (lobos.core/rollback)
