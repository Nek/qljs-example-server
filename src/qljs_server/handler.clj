(ns qljs-server.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.util.response :refer [response content-type]]
            [duratom.core :refer [duratom]]
            ))

(def state (duratom :local-file
                    :file-path "db"
                    :init {:todos {} :areas {0 {:title "Chores"} 1 {:title "Today"}}}))

(defn uuid [] (str (java.util.UUID/randomUUID)))

(defn add-todo! [todo]
  (let [id (uuid)]
    (swap! state assoc-in [:todos id] todo)
    id))

(defn delete-todo! [id]
  (swap! state update-in [:todos] dissoc id))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/todos" [] (-> @state
                       (response)
                       (content-type "application/json")))
  (POST "/todos" req
        (response {
                   :status "Success"
                   :id (add-todo! (:body req))}))
  (DELETE "/todos/:id" [id]
          (delete-todo! id)
          (response {:status "Success"}))
  (route/not-found "Not Found"))

(def app
  (-> app-routes
      (wrap-defaults api-defaults)
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))
