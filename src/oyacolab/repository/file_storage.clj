(ns oyacolab.repository.file-storage
  (:require [amazonica.core :refer :all]
            [amazonica.aws.s3 :refer :all]
            [environ.core :refer [env]]))

(defn save [k file]
  (put-object
   :bucket-name "oyacolab"
   :key k
   :file file
   :access-control-list {:grant-permission ["AllUsers" "Read"]})
  {:id k :file-name (.getName file)})
