(def constant constantly)
(defn variable [name] #(get % name))
(defn make-op [op] (fn [& operands] (fn [vals] (apply op (mapv #(% vals) operands)))))
(def add (make-op +))
(def subtract (make-op -))
(def multiply (make-op *))
;; не принимает 0 аргументов
;; лучше через паттерн матчинг
(defn correctDiv [& args]
  (if (== (count args) 1)
    (/ 1 (double (first args)))
    (reduce #(/ (double %1) (double %2)) args)))
(def div (make-op correctDiv))
(def divide div)
(defn negate [arg] #(- (arg %)))
(def exp (make-op #(Math/exp %)))
(def log (make-op #(Math/log %)))
(defn sumexp-impl [operands] (apply add (mapv exp operands)))

(defn sumexp [& operands] (sumexp-impl operands))

(defn lse [& operands] (log (sumexp-impl operands)))


(def get-op
  {'+      add
   '-      subtract
   '*      multiply
   '/      divide
   'negate negate
   'sumexp sumexp
   'lse    lse})
(defn parserTemplate [cnst-ctor var-ctor ops]
  (fn [expr]
    (letfn [(parseToken [token]
              (cond
                (number? token) (cnst-ctor token)
                (list? token) (apply (ops (first token)) (mapv parseToken (rest token)))
                (symbol? token) (var-ctor (str token))
                ))]
      (parseToken (read-string expr)))))
(def parseFunction (parserTemplate constant variable get-op))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(load-file "proto.clj")
(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))
(def value (field :value))
(def name (field :name))
(def varName (field :varName))
(def goodVarName (field :goodVarName))
(def operator (field :operator))
(def function (field :function))
(def operands (field :operands))
(def diffOp (field :diffOp))
(def countArgs (field :countArgs))

(defn Expression [evaluate toString toStringInfix diff]
  {:evaluate evaluate :toString toString :toStringInfix toStringInfix :diff diff})
(declare ZERO)
(declare Multiply Divide Sumexp Lse)
(def Constant
  (constructor (fn [this value] (assoc this :value value))
               (Expression
                 (fn [this _] (value this))
                 (fn [this] (str (value this)))
                 (fn [this] (str (value this)))
                 (fn [_ _] ZERO)
                 )))
(def ZERO (Constant 0))
(def ONE (Constant 1))

(def Variable
  (constructor (fn [this varName] (assoc this :varName varName :goodVarName (clojure.string/lower-case (str (first (char-array varName))))))
               (Expression
                 (fn [this vars] (get vars (goodVarName this)))
                 (fn [this] (varName this))
                 (fn [this] (varName this))
                 (fn [this diffName] (if (= (goodVarName this) diffName) ONE ZERO)))))
(def Operation
  (constructor
    (fn [this function operator diffOp] (assoc this :function function :operator operator :diffOp diffOp))
    (Expression
      (fn [this vars] (apply (function this) (mapv #(evaluate % vars) (operands this))))
      (fn [this] (str "("
                      (operator this) " " (clojure.string/join " " (mapv #(toString %) (operands this)))
                      ")"))
      (fn [this] (if (= (countArgs this) 1)
                   (str (operator this) " " (toStringInfix (first (operands this))))
                   (str "(" (toStringInfix
                              (first (operands this))) (apply str (mapv #(str " " (operator this) " " (toStringInfix %))
                                                                        (subvec (operands this) 1))) ")")))
      (fn [this diffName] ((diffOp this) (mapv #(diff % diffName) (operands this)) (operands this))))))
(defn make-obj-op [function operator diffOp]
  (constructor
    (fn [this & args] (assoc this :operands (vec args) :countArgs (count args)))
    (Operation function operator diffOp)))
(def Add (make-obj-op + "+" (fn [f' _] (apply Add f'))))
(def Subtract (make-obj-op - "-" (fn [f' _] (apply Subtract f'))))
(def Negate (make-obj-op #(- %) "negate" (fn [f' _] (apply Negate f'))))
(defn mul-diff [f' f]
  (apply Add (mapv
               (fn [i] (apply Multiply (assoc f i (f' i))))
               (range (count f')))))
(def Multiply (make-obj-op * "*" mul-diff))
(defn div-diff [f' f]
  (let [a' (first f') a (first f)
        g' (subvec f' 1) g (subvec f 1)
        z (apply Multiply g)]
    (if (empty? g)
      (div-diff [ZERO a'] [ONE a])
      (Divide (Subtract (Multiply a' z) (Multiply a (mul-diff g' g))) (Multiply z z)))))
(def Divide (make-obj-op correctDiv "/" div-diff))
(defn meansq [& args] (/ (apply + (mapv #(* % %) args)) (count args)))
(defn sum-prod-f-f' [f' f] (Divide
                             (apply Add (mapv #(Multiply %1 %2) f' f))
                             (Constant (count f))))
(def Meansq
  (make-obj-op
    meansq
    "meansq"
    (fn [f' f]
      (Multiply
        (Constant 2)
        (sum-prod-f-f' f' f)))))
(def RMS
  (make-obj-op
    (fn [& args] (Math/sqrt (apply meansq args)))
    "rms"
    (fn [f' f]
      (Divide
        (sum-prod-f-f' f' f)
        (apply RMS f)))))
(defn toBool [f & args]
  (if (reduce f (mapv #(> % 0) args)) 1 0))
(def And
  (make-obj-op
    (partial toBool #(Boolean/logicalAnd %1 %2))
    "&&"
    (fn [_ _] nil)))

(def Or
  (make-obj-op
    (partial toBool #(Boolean/logicalOr %1 %2))
    "||"
    (fn [_ _] nil)))
(def Xor
  (make-obj-op
    (partial toBool #(Boolean/logicalXor %1 %2))
    "^^"
    (fn [_ _] nil)))
(def Not
  (make-obj-op
    (fn [arg] (if (> arg 0) 0 1))
    "!"
    (fn [_ _] nil)))

(def get-obj-op {
                 '+      Add
                 '-      Subtract
                 '*      Multiply
                 '/      Divide
                 'negate Negate
                 'meansq Meansq
                 'rms    RMS
                 (symbol "&&") And
                 (symbol "||") Or
                 (symbol "^^") Xor
                 (symbol "!") Not
                 })
(def parseObject (parserTemplate Constant Variable get-obj-op))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 12 HW
(load-file "parser.clj")
(defn +take [s] (+str (apply +seq (mapv #(+char (str %)) (char-array s)))))
(defn *filter [pre]
  (+char (apply str (filter pre (mapv char (range 32 128))))))
(def *unsigned
  (+plus (*filter #(Character/isDigit %)))
  )
(def *ws
  (+ignore (+star (*filter #(Character/isWhitespace %)))))

(defn *skip_ws [parser]
  (+seqn
    0
    *ws
    parser
    *ws
    ))
(def *digits
  (+map
    read-string
    (+str (+seqf
            #(flatten %&)
            (+opt (+char "-"))
            *unsigned
            (+opt (+char "."))
            (+opt *unsigned)))))
(defn *brackets [parser]
  (+seqn
    1
    (*skip_ws (+char "("))
    parser
    (*skip_ws (+char ")"))))
(def *const
  (+map Constant (*skip_ws *digits)))
(def *var
  (+map Variable (+str (*skip_ws (+plus (+char "xyzZXY"))))))
(def *take-op #(+map get-obj-op (+map symbol (*skip_ws (+take %)))))
(defn *bin-op [operators next-level-parser]
  (+map
    #(reduce (fn [acc [cnstr expr]] (cnstr acc expr)) (first %) (nth % 1))
    (+seq
      next-level-parser
      (+star (+seq operators next-level-parser)))))

(declare *parse-not-bin-op)
(defn parse-unary [f name ]
  (+map f (+seqn 1 (*take-op name) (delay *parse-not-bin-op))))
(def *negate (+map Negate (+seqn 1 (*take-op "negate") (delay *parse-not-bin-op))))
(def *not (+map Not (+seqn 1 (*take-op "!") (delay *parse-not-bin-op))))
(declare *expression)
(def *parse-not-bin-op (+or *const *var (*brackets (delay *expression)) *negate *not))
(def *parse-mul-div (*bin-op (+or (*take-op "/") (*take-op "*")) *parse-not-bin-op))
(def *parse-plus-minus (*bin-op (+or (*take-op "+") (*take-op "-")) *parse-mul-div))
(def *parse-and (*bin-op (*take-op "&&") *parse-plus-minus))
(def *parse-or (*bin-op (*take-op "||") *parse-and))
(def *parse-xor (*bin-op (*take-op "^^") *parse-or))
(def *expression *parse-xor)

(def parseObjectInfix (+parser *expression))
