"use strict";

const cnst = value => x => value;
const variable = () => x => x;
const binary = f => (a, b) => x => f(a(x), b(x));
const add = binary((a, b) => a + b);
const subtract = binary((a, b) => a - b)
const multiply = binary((a, b) => a * b)
const divide = binary((a, b) => a / b)
const negate = f => x => -(f(x))

let expr = add(
    subtract(
        multiply(
            variable("x"),
            variable("x")
        ),
        multiply(
            cnst(2),
            variable("x")
        )
    ),
    cnst(1)
)

for (let i = 0; i < 11; i++) {
    console.log(expr(i))
}