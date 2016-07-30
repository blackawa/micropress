(ns user
  (:require [alembic.still :as a]
            [micropress.core :refer :all]))

(defn reload-dep []
  (a/load-project))
