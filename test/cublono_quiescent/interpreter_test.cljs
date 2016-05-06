;   Copyright (c)  Sylvain Tedoldi. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.
;

(ns cublono-quiescent.interpreter-test
  (:require-macros [cljs.test :refer [is are deftest testing]])
  (:require
    cljsjs.react.dom.server
    cljsjs.react-bootstrap
    [hickory.core :as hickory]
    [cublono-quiescent.interpreter :as i]
    cljs.test))

(defn- render-static
  "Render `element` as HTML string, without React internal attributes."
  [element]
  (if element
    (js/ReactDOMServer.renderToStaticMarkup element)))

(defn- interpret [element]
  (some->> element
           i/interpret
           render-static
           hickory/parse-fragment
           (map hickory/as-hiccup)
           first))

(deftest test-simple-div
  (is (= [:div {:class "a b c"}]
         (interpret [:div.a.a.b.b.c {:class "c"}]))))

(deftest test-children
  (is (= [:div {:class "a b c"} "childchild2"]
         (interpret [:div.a.a.b.b.c {:class "c"} "child" "child2"]))))

(deftest test-unknown-children-as-is
  (is (= [:div {:class "a b c"} "child" [:div {:class "well"} "well"] "child2"]
         (interpret [:div.a.a.b.b.c {:class "c"}
                     "child"
                     (js/React.createElement js/ReactBootstrap.Well. nil "well")
                     "child2"]))))

(deftest test-deep
  (is (=  [:div {:class "a b c"}
           "child"
           [:span {} "deep"]]
         (interpret [:div.a.a.b.b.c {:class "c"}
                     "child"
                     [:span {} "deep"]]))))


(deftest test-bootstrap-tag
  (is (= [:div {:class "a b c alert alert-info" :role "alert"}]
         (interpret [:ReactBootstrap/Alert.a.a.b.b.c {:class "c"}]))))

(deftest test-mapped
  (is (= [:div {:class "a b c alert alert-info" :role "alert"}
          "a"
          [:div {:class "d well"} "b"]
          [:div {:class "d well"} "c"]]
         (interpret [:ReactBootstrap/Alert.a.a.b.b.c {:class "c" :key :alert}
                     "a"
                     (map (fn [%] [:ReactBootstrap/Well.d {:key %} %])
                          ["b" "c"])]))))
