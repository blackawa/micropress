(ns micropress.const.authorization)

(def uri-method-defs
  "認可のミドルウェアがこのシーケンスを読んで、あるユーザーがアクセス可能かどうか判断する."
  [{:request-method :post   :uri "/api/invitation"           :auth [1]}
   {:request-method :get    :uri "/api/invitation"           :auth [1]}
   {:request-method :delete :uri #"^/api/invitation/.*$"     :auth [1]}
   {:request-method :get    :uri "/api/user"                 :auth [1 2 3]}
   {:request-method :get    :uri #"^/api/user/.*$"           :auth [1 2 3]}
   {:request-method :put    :uri #"^/api/user/.*$"           :auth [1 2 3]}
   {:request-method :post   :uri "/api/draft"                :auth [    3]}
   {:request-method :post   :uri #"^/api/draft/.$"           :auth [    3]}])
