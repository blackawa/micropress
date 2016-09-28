(ns micropress.repository.invitee
  (:import [java.sql SQLException])
  (:require [clj-time.core :as c]
            [clj-time.jdbc]
            [korma.core :refer [insert values
                                select fields
                                delete
                                where]]
            [micropress.entity :as e]))

(defn insert-invitee
  [token email auth]
  (let [expire-time (c/plus (c/now) (c/days 1))
        invitee-id (:generated_key (insert e/invitees (values {:invitation_token token :email_address email :expire_time expire-time})))]
    (insert e/invitee-authorities
            (values (map (fn [authorities-id] {:invitees_id invitee-id :authorities_id authorities-id}) auth)))
    {:ok? true :invitee-id invitee-id}))

(defn find-all
  []
  (select e/invitees
          (fields :id :email_address :expire_time)))

(defn find-by-token
  [token]
  (->> (select e/invitees
               (where {:invitation_token token}))))

(defn find-by-id
  [invitee-id]
  (first (select e/invitees (where {:id invitee-id}))))

(defn delete-by-id
  [invitee-id]
  (delete e/invitees (where {:id invitee-id})))
