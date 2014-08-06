(require '[clojure.java.jdbc :as jdbc])

(def create-users-table-sql
  "create table users (
    id bigserial primary key,
    username varchar(255),
    email varchar(255) not null
  );")

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

(jdbc/execute! pgdb [create-users-table-sql])
