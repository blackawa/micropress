(ns micropress.service.article
  (:require [clj-time.core :as time]
            [clj-time.format :as f]
            [clj-time.local :as l]
            [micropress.repository.article :as article]))

(defn publish
  [article-id publish-at]
  (article/publish-article article-id
                           (if (nil? publish-at)
                             (time/now)
                             (l/to-local-date-time (f/parse (f/formatter "yyyyMMdd HHmmss") publish-at)))))
