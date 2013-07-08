(ns mini-kanren.core
  (:use clojure.core.logic)
  (:refer-clojure :exclude [==]))

;;; borrowed from
;;; https://github.com/candera/reasoned-schemer

(defn set=
  "Returns true if a and b have the same elements, regardless of order"
  [a b]
  (= (set a) (set b)))

(defn pair?
  "Returns true if x is a pair-like thing. The slightly awkward
  definition arises out of the mismatch between Scheme and Clojure."
  [x]
  (or (lcons? x) (and (coll? x) (seq x))))

(defn pairo
  "Succeeds if p is a pair-like thing."
  [p]
  (fresh [a d]
    (== (lcons a d) p)))

(defn listo
  [l]
  (conde
   ((emptyo l) s#)
   ((pairo l) (fresh [d]
                     (resto l d)
                     (listo d)))
   ((s# u#))))