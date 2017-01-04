(ns oyacolab.endpoint.api
  (:require [cljs.reader :refer [read-string]]
            [clojure.browser.net :as net]
            [goog.events :as events])
  (:import [goog.net EventType]
           [goog.net.XhrIo ResponseType]))

(defn request [url method handler & {:keys [error-handler body headers]}]
  (let [xhrio (net/xhr-connection)]
    (events/listen xhrio EventType.SUCCESS
                   (fn [e]
                     (handler xhrio)))
    (events/listen xhrio EventType.ERROR
                   (fn [e] (if (nil? error-handler) nil (error-handler e xhrio))))
    (.send xhrio
           url
           (.toLowerCase (name method))
           body
           (clj->js (merge {} headers)))))
