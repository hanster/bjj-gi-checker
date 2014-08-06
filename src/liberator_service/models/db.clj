(ns liberator-service.models.db
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [liberator-service.models.schema :as schema]))


(defdb db schema/db-spec)

(defentity brands)
