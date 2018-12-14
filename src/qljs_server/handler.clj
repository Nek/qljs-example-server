(ns qljs-server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response content-type]]))

(def state (atom {
                  :todos {0 {:text "Buy milk" :area 0}
                          1 {:text "Do dishes" :area 1}}
                  :areas {0 {:title "Chores"}
                          1 {:title "Today"}}}))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/todos" [] (-> @state
                       (response)
                       (content-type "application/json")))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults site-defaults)
      (wrap-json-response)))
