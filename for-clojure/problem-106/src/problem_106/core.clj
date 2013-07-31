(ns problem-106.core
  (:require clojure.string
            clojure.pprint)
  (:gen-class))

(defmacro pdump [x]
  `(let [x# ~x]
     (do (println "----------------")
         (clojure.pprint/pprint '~x)
         (println "~~>")
         (clojure.pprint/pprint x#)
         (println "----------------")
         x#)))

;;;  ___         _    _             _  __   __
;;; | _ \_ _ ___| |__| |___ _ __   / |/  \ / /
;;; |  _/ '_/ _ \ '_ \ / -_) '  \  | | () / _ \
;;; |_| |_| \___/_.__/_\___|_|_|_| |_|\__/\___/


(def __ (letfn [(d [n] (* n 2)) ; double
                (h [n] (/ n 2)) ; halve
                (a [n] (+ n 2)) ; add
                (hop-count [n t m hops-so-far]
                  (cond
                   (= n t)            hops-so-far ; end search
                   (>= hops-so-far m) hops-so-far ; limit depth
                   :else
                   (let [nh (inc hops-so-far) ; take a hop
                         dh (hop-count (d n) t m nh)
                         ah (hop-count (a n) t m nh)
                         hh (when (even? n) (hop-count (h n) t m nh))]
                     (cond
                      (odd? n)  (min dh ah)
                      (even? n) (min dh ah hh)))))]
          (fn [s t] (hop-count s t 10 1))))

(def __ #((fn r [n s]
            (if ((set s) %2) n
                (r (+ n 1)
                   (for [f [+ * /] e s] (f e 2)))))
          1 [%]))

(def __ (letfn [(d [n] (* n 2)) ; double
                (h [n] (/ n 2)) ; halve
                (a [n] (+ n 2)) ; add
                (hop-count [n ts hops]
                  (cond
                   (ts n)      hops ; end search
                   :else
                   (hop-count n (set (concat ts (map d ts) (map a ts) (map h ts))) (inc hops))))]
          (fn [s t] (hop-count t #{s} 1))))

(println (map #(apply __ %) [[1 1] [3 12] [12 3] [5 9] [9 2] [9 12]]))

(+ 2 3)
(take 5 [1 2 3 4 5 6])
(concat [2 3] [5 7])
(take 5 (range 5 java.lang.Integer/MAX_VALUE 2))
(for [cand [2 3 4]] cand)
(every? even? [4 2])
(range 5 100 2)
(take 15 (iterate inc 2))

(def driver-table
  (pdump (map (partial zipmap [:chan :pl :driver-name :asin :prime :amount])
              [[1000 23 :prod-cogs "B00012345" :p1 5]
               [1000 23 :prod-cogs "B00012346" :p2 15]
               [1000 23 :prod-cogs "B00012347" :p3 10]
               [1100 24 :prod-rev  "B00012348" :p1 19]
               ])))

(def driver-mapping
  "Maps accounts to drivers."
  (pdump (map (partial zipmap [:chan :pl :account :driver-name :homo?])
              [[1000 23 :prod-cogs :prod-cogs true]
               [1000 23 :inv-val   :prod-cogs false]])))

(defn normalize [amaps]
  (let [total (apply + (map :amount amaps))]
    (map
     (fn [amap] (assoc amap :amount (/ (:amount amap) total)))
     amaps)))

(defn driver [driver-name chan pl]
  (let [ripped-table
        (filter (fn [line] (and (= driver-name (:driver-name line))
                               (= chan        (:chan        line))
                               (= pl          (:pl          line))))
                driver-table)
        ]
    ripped-table))

(def pnls
  (pdump (map (partial zipmap [:chan :pl :account :amount])
              [[1000 23 :prod-cogs 2500]
               [1600 23 :inv-val   5000]
               ])))

(defn driver-spec [driver-name pnl]
  (normalize (driver driver-name (:chan pnl) (:pl pnl))))

(pdump (driver-spec :prod-cogs (first pnls)))



(defn allocate [pnl driver-spec]
  (map (fn [line]
         (assoc line :amount (* (:amount pnl) (:amount line))))
       driver-spec))

(pdump (allocate (first pnls)
                 (driver-spec :prod-cogs (first pnls))))

(defn fact [n acc]
  (if
    (< n 2N)
    acc
    (recur (dec n) (* acc n))))

(take 4 (drop 100 (letfn [(test [n primes]
                            (every? (fn [p] (not= 0 (mod n p)))
                                    (take-while
                                     (fn [p] (<= (* p p) n))
                                     primes))
                            )]
                    (reduce (fn [primes candidate]
                              (if (test candidate primes)
                                (conj primes candidate)
                                primes
                                ))
                            [2 3]
                            (range 5N 1000N 2))
                    )))

((fn [comb & hms]
   (reduce
    (fn [dest src]
      (into dest
            (reduce
             (fn [hm [k v]]
               (assoc hm k
                      (let [curr (dest k)]
                        (if curr (comb curr v) v))))
             {}
             src)))
    hms))
 *
 {:a 2, :b 3, :c 4},
 {:a 2},
 {:b 2},
 {:c 5}
 )

(defn sieve [xs]
  (let [x (first xs)]
       (cons x
             (lazy-seq (sieve 
                        (filter #(not= 0 (mod % x))
                                (rest xs)))))))

(def primes (sieve (cons 2 (iterate (partial + 2N) 3))))

(pdump (take 5 (drop 1000 primes)))

(def testo (fn [n] (letfn [(sieve [primes]
                            (let [p (first primes)]
                              (cons p (lazy-seq (sieve (filter #(not= 0 (mod % p))
                                                               (rest primes)))))
                              ))]
                    (take n (sieve (cons 2 (iterate (partial + 2) 3)))))))

((fn [sent])
 "Have a nice day.")

;; primes = [2, 3] ++ [ cand | cand <- [5, 7..],
;;                             all (\p -> notDvbl cand p)
;;                                 (takeWhile (\p -> p*p < cand) primes) ]

