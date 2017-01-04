(ns oyacolab.endpoint.api
  (:require [compojure.core :refer :all]
            [oyacolab.resource.editor :refer [editor]]
            [oyacolab.resource.customer.article :as customer-article]
            [oyacolab.resource.customer.articles :as customer-articles]
            [oyacolab.resource.admin.auth-token :refer [auth-token]]
            [oyacolab.resource.admin.articles :as admin-articles]
            [oyacolab.resource.admin.article :as admin-article]
            [oyacolab.resource.admin.authentication :refer [authentication]]
            [oyacolab.resource.admin.file :as file]))

(defn endpoint [{{db :spec} :db}]
  (context "/api" _
           (ANY "/editor" _ (editor db))
           (ANY "/articles" _ (customer-articles/articles db))
           (ANY "/articles/:id" _ (customer-article/article db))
           (ANY "/admin/auth-token" _ (auth-token db))
           (ANY "/admin/authentication" _ (authentication db))
           (ANY "/admin/articles" _ (admin-articles/articles db))
           (ANY "/admin/articles/:id" _ (admin-article/article db))
           (ANY "/admin/file" _ (file/file db))))
