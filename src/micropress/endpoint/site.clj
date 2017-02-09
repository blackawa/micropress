(ns micropress.endpoint.site
  (:require [compojure.core :refer :all]
            [hiccup.page :refer [html5 include-js]]
            [ring.util.response :refer [redirect]]))

(defn- index []
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:link {:rel "stylesheet" :href "/assets/normalize.css/normalize.css"}]
    [:link {:rel "stylesheet" :href "/assets/Semantic-UI/semantic.min.css"}]
    [:link {:rel "stylesheet" :href "/style.css"}]
    [:title "console | micropress"]]
   [:body
    [:div {:id "app"}
     [:p "loading..."]]
    (include-js "/js/main.js")
    [:script "micropress.main.init();"]]))

(defn endpoint [{{db :spec} :db}]
  (routes
   (GET "/" [] (redirect "/login"))
   (GET "/login" [] (index))
   (GET "/admin/articles" [] (index))
   (GET "/admin/articles/new" [] (index))
   (GET "/admin/articles/:id" [] (index))
   (GET "/admin/editors" [] (index))
   (GET "/admin/editors/new" [] (index))
   (GET "/admin/editors/:id" [] (index))
   (GET "/admin/profile" [] (index))
   (GET "/invitation/:token" [] (index))))
