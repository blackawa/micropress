(ns micropress.util.url
  (:require [goog.dom :as dom ]))

(defn parse-uri [url]
  (let [link (dom/createDom "a" (clj->js {"href" url}))]
    (.-pathname link)))
