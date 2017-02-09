(ns micropress.endpoint.api
  (:require [compojure.core :refer :all]
            [micropress.resource.customer.article :as customer-article]
            [micropress.resource.customer.articles :as customer-articles]
            [micropress.resource.admin.auth-token :refer [auth-token]]
            [micropress.resource.admin.articles :as admin-articles]
            [micropress.resource.admin.article :as admin-article]
            [micropress.resource.admin.authentication :refer [authentication]]
            [micropress.resource.admin.invitations :as admin-invitations]
            [micropress.resource.public.invitation :as public-invitation]
            [micropress.resource.admin.editors :refer [editors]]
            [micropress.resource.admin.editor :refer [editor]]
            [micropress.resource.admin.file :refer [file]]
            [micropress.resource.admin.profile :refer [profile]]))

(defn endpoint [{{db :spec} :db}]
  (context "/api" _
           (ANY "/articles" _ (customer-articles/articles db))
           (ANY "/articles/:id" _ (customer-article/article db))
           (ANY "/invitations/:token" _ (public-invitation/invitation db))
           (ANY "/admin/invitations" _ (admin-invitations/invitations db))
           (ANY "/admin/editors" _ (editors db))
           (ANY "/admin/editors/:id" _ (editor db))
           (ANY "/admin/auth-token" _ (auth-token db))
           (ANY "/admin/authentication" _ (authentication db))
           (ANY "/admin/articles" _ (admin-articles/articles db))
           (ANY "/admin/articles/:id" _ (admin-article/article db))
           (ANY "/admin/file" _ (file db))
           (ANY "/admin/profile" _ (profile db))))
