(defproject liberator-service "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [liberator "0.10.0"]
                 [cheshire "5.2.0"]
                 [lib-noir "0.8.3"]
                 [enlive "1.1.5"]
                 [sicia/lobos "1.0.0-beta2"]
                 [korma "0.3.3"]
                 [postgresql "9.3-1101.jdbc4"]
                 [prismatic/schema "0.3.0"]
                 [clj-time "0.8.0"]]
  :plugins [[lein-ring "0.8.10"]]
  :ring {:handler liberator-service.handler/app
         :init liberator-service.handler/init
         :destroy liberator-service.handler/destroy}
  :profiles
  {:production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.2.1"]]}})
