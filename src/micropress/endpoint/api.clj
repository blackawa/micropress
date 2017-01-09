(ns micropress.endpoint.api
  (:require [compojure.core :refer :all]
            [micropress.resource.editor :refer [editor]]
            [micropress.resource.customer.article :as customer-article]
            [micropress.resource.customer.articles :as customer-articles]
            [micropress.resource.admin.auth-token :refer [auth-token]]
            [micropress.resource.admin.articles :as admin-articles]
            [micropress.resource.admin.article :as admin-article]
            [micropress.resource.admin.authentication :refer [authentication]]
            [micropress.resource.admin.file :as file]))

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
