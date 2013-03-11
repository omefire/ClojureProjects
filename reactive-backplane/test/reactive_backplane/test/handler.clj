(ns reactive-backplane.test.handler
  (:use clojure.test
        ring.mock.request  
        reactive-backplane.handler))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) "{\"hello\":\"json get\"}"))))
  
  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))