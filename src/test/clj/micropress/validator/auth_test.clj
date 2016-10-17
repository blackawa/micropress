(ns micropress.validator.auth-test
  (:require [clojure.test :refer :all]
            [micropress.validator.auth :as target]))

;; === pwd-input? ======================================

(deftest pwd-input?-green
  (let [{:keys [ok? messages]} (target/pwd-input? "input" :pwd)]
    (is ok?)
    (is (nil? (-> messages first :message)))))

(deftest pwd-input?-blank
  (let [{:keys [ok? messages]} (target/pwd-input? " " :pwd)]
    (is (not ok?))
    (is (= 1 (count messages)))))

;; === validate-auth ===================================

(deftest validate-auth-green
  (let [{:keys [ok? messages]} (target/validate-auth "green@micropress.com" "green")]
    (is ok?)))

(deftest validate-auth-nil
  (let [{:keys [ok? messages]} (target/validate-auth nil nil)]
    (is (not ok?))
    (is (= 2 (count messages)))))

(deftest validate-auth-invalid
  (let [{:keys [ok? messages]} (target/validate-auth "invalid@" "")]
    (is (not ok?))
    (is (= 2 (count messages)))))
