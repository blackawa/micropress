(ns micropress.sub
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub :route (fn [db _] (:route db)))
(reg-sub :data (fn [db _] (:data db)))
(reg-sub :form (fn [db _] (:form db)))
(reg-sub :error (fn [db _] (:error db)))
(reg-sub :flush (fn [db _] (:flush db)))
