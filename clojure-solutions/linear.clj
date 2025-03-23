(defn template-checker [coll type & args]
  (let [checker (partial coll type)]
    (and (every? checker args) (apply == (map count args)))))
(defn is-vector? [checker vector] (and (vector? vector) (every? checker vector)))

(defn function [checker operation]
  (fn [& args]
    {:pre [(every? (partial checker (first args)) args)]}
    (apply mapv operation args)))
(defn apply-operation [operation] (fn [& args] (reduce operation args)))
(def vector-checker (partial template-checker is-vector? number?))
(def vector-function (partial function vector-checker))
(def v+ (vector-function +))
(def v- (vector-function -))
(def v* (vector-function *))
(def vd (vector-function /))
(defn scalar [& vectors] (reduce + (apply v* vectors)))

(def vect (apply-operation (fn [v1 v2]
                             {:pre [(vector-checker v1 v2)]}
                             (let [[x1 y1 z1] v1 [x2 y2 z2] v2]
                               [(- (* y1 z2) (* y2 z1))
                                (- (* z1 x2) (* z2 x1))
                                (- (* x1 y2) (* x2 y1))]))))
(defn v*s [vector & scalars]
  {
   }
  (let [scalar (reduce * scalars)]
    (mapv (partial * scalar) vector)))
(defn is-matrix? [matrix] (is-vector? #(and (vector? %) (partial vector-checker %)) matrix))
(defn matrix-checker [matrix1 matrix2]
  (and (is-matrix? matrix1) (is-matrix? matrix2)
       (template-checker every? vector? matrix1 matrix2)
       (vector-checker (first matrix1) (first matrix2))))
(defn compatible? [x y]
  (== (count y) (count (first x))))
(def matrix-function (partial function matrix-checker))
(def m+ (matrix-function v+))
(def m- (matrix-function v-))
(def m* (matrix-function v*))
(def md (matrix-function vd))
(defn m*s [matrix & scalars]
  {:pre [(every? number? scalars)]}
  (let [scalar (apply * scalars)]
    (mapv #(v*s % scalar) matrix)))
(defn m*v [matrix vector]
  (mapv (partial scalar vector) matrix))

(defn transpose [matrix]
  {:pre  [(and (vector? matrix) (every? vector? matrix))]
   }
  (apply mapv vector matrix))
(def m*m
  (apply-operation
    (fn [matrix1 matrix2]
      {
       :post [ (== (count (first %)) (count (first matrix2)))]}
      (mapv (partial m*v (transpose matrix2)) matrix1))))
(defn tensor-checker [a b]
  (or (and (number? a) (number? b)) (vector-checker a b)
      (and
        (template-checker is-vector? vector? a b)
        (every? true? (mapv tensor-checker a b)))))

(defn recursive-traversal [op]
  (letfn [(fun [& args] (if (every? number? args) (apply op args) (apply mapv fun args)))]
    fun))

(defn apply-operation-tensor [e fun]
  (fn [& args]
    (if (== (count args) 1) ((recursive-traversal (partial fun e)) (first args)) (reduce fun args))))

(defn tensor-function [op e]
  (apply-operation-tensor e (fn [x y]
                              {:pre [(tensor-checker x y)]}
                              ((recursive-traversal op) x y))))

(def t+ (tensor-function + 0))
(def t- (tensor-function - 0))
(def t* (tensor-function * 1))
(def td (tensor-function / 1))
(def p 10)
