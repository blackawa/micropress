(ns micropress.util.validator-test
  (:require [clojure.test :refer :all]
            [micropress.util.validator :as target]))

;; === aggregate ==================================
(deftest aggregate-true-results
  (let [{:keys [ok? messages]} (target/aggregate
                              {:ok? true :messages [{:target :t1 :message nil}]}
                              {:ok? true :messages [{:target :t2 :message nil}]}
                              {:ok? true :messages [{:target :t3 :message nil}]})]
    (is ok?)
    (is (empty? messages))))

(deftest aggregate-include-false
  (let [{:keys [ok? messages]} (target/aggregate
                                {:ok? true :messages [{:target :t1 :message nil}]}
                                {:ok? false :messages [{:target :t2 :message "t2 is invalid"}]}
                                {:ok? false :messages [{:target :t3 :message "t3 is invalid"}]})]
    (is (not ok?))
    (is (= 2 (count messages)))))

(deftest aggregate-multiple-msg-in-one-aggregatee
  (let [{:keys [ok? messages]} (target/aggregate
                                {:ok? false :messages [{:target :t1 :message "t1 is invalid"}]}
                                {:ok? false :messages [{:target :t2 :message "t2 is invalid because of reason 1"}
                                                       {:target :t2 :message "t2 is invalid because of reason 2"}
                                                       {:target :t2 :message "t2 is invalid because of reason 3"}]})]
    (is (not ok?))
    ;; まだOutputの形を担保するスペックになってない
    (is (= 4 (count messages)))))

;; 理想的にはこういう戻り値がほしいなー...
;; 今から変えられるのか...?
;; このmap作るのそこそこ面倒やな...
;; このフォーマットにすることでクライアントサイドでどんないいことが あるかというと、
;; targetに渡したキー名でmessages内部を検索するだけでメッセージが取得できる.
;; 今のフォーマットでは、messages内の各mapについて、その内部に入ってtargetキーと
;; 項目キー名の突き合わせを自分で実装しなくちゃいけない...
{:status 400
 :messages {:t1 ["t1 is invalid"]
            :t2 ["t2 is invalid for reason 1" "t2 is invalid for reason 2"]}}
