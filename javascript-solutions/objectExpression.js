"use strict";

const VARIABLES = {'x': 0, 'y': 1, 'z': 2};

function Expression(string, prefixString, postfixString, differential, evaluation) {
    this.string = string;
    this.prefixString = prefixString;
    this.postfixString = postfixString;
    this.differential = differential;
    this.evaluation = evaluation;
}

Expression.prototype.evaluate = function () {
    return this.evaluation;
}
Expression.prototype.toString = function () {
    return this.string
}
Expression.prototype.prefix = function () {
    return this.prefixString;
}

Expression.prototype.postfix = function () {
    return this.postfixString;
}

function Const(val) {
    Expression.call(this, String(val), String(val), String(val), 0, val);
}

Const.prototype = Object.create(Expression.prototype);
Const.prototype.diff = function () {
    return new Const(this.differential);
}

const CONST = {'0': new Const(0), '1': new Const(1), '2': new Const(2)};

function Variable(name) {
    Expression.call(this, name, name, name, (varDiff) => varDiff === name ? 1 : 0);
}

Variable.prototype = Object.create(Expression.prototype);
Variable.prototype.evaluate = function (...args) {
    return args[VARIABLES[this.string]];
}
Variable.prototype.diff = function (varDif) {
    return varDif === this.string ? CONST['1'] : CONST['0'];
}

function Operation(operator, operation, diffOp, ...exprs) {
    Expression.call(
        this,
        exprs.map(expr => expr.toString()).join(' ') + ' ' + operator,
        '(' + operator + ' ' + exprs.map(expr => expr.prefixString).join(' ') + ')',
        '(' + exprs.map(expr => expr.postfixString).join(' ') + ' ' + operator + ')',
        varDiff => diffOp(varDiff, ...exprs),
        (x, y, z) => operation(...exprs.map(expr => expr.evaluate(x, y, z)))
    );
}

Operation.prototype = Object.create(Expression.prototype);
Operation.prototype.evaluate = function (x, y, z) {
    return this.evaluation(x, y, z);
}
Operation.prototype.diff = function (varDiff) {
    return this.differential(varDiff);
}

function Negate(val) {
    Operation.call(
        this,
        'negate',
        a => -a,
        varDiff => new Negate(val.diff(varDiff)),
        val
    );
}

Negate.prototype = Object.create(Operation.prototype);

function Add(left, right) {
    Operation.call(
        this,
        '+',
        (left, right) => left + right,
        varDiff => new Add(left.diff(varDiff), right.diff(varDiff)),
        left, right
    );
}

Add.prototype = Object.create(Operation.prototype);

function Subtract(left, right) {
    Operation.call(
        this,
        '-',
        (left, right) => left - right,
        varDiff => new Subtract(left.diff(varDiff), right.diff(varDiff)),
        left, right
    );
}

Subtract.prototype = Object.create(Operation.prototype);

function Multiply(left, right) {
    Operation.call(
        this,
        '*',
        (left, right) => left * right,
        varDiff => new Add(new Multiply(left.diff(varDiff), right), new Multiply(left, right.diff(varDiff))),
        left, right
    );
}

Multiply.prototype = Object.create(Operation.prototype);

function Divide(left, right) {
    Operation.call(
        this,
        '/',
        (left, right) => left / right,
        varDiff => new Divide(
            new Subtract(
                new Multiply(left.diff(varDiff), right),
                new Multiply(left, right.diff(varDiff))
            ),
            new Multiply(right, right)
        ),
        left, right
    );
}

Divide.prototype = Object.create(Operation.prototype);
const sumArgs = (...args) => args.reduce(function (sum, elem) {
        return sum + elem;
    }, 0
);
const sumsqArgs = (...args) => sumArgs(...args.map(arg => arg * arg));

function Sum(n, ...args) {
    Operation.call(
        this,
        'sum' + n,
        (...args) => sumArgs(...args),
        (varDiff, ...args) => new Sum(n, ...args.map(arg => arg.diff(varDiff))),
        ...args
    );
}

Sum.prototype = Object.create(Operation.prototype);

function Sumsq(n, ...args) {
    Operation.call(
        this,
        'sumsq' + n,
        (...args) => sumsqArgs(...args),
        (varDiff, ...args) => new Sum(n, ...args.map(
            arg => new Multiply(new Multiply(CONST['2'], arg), arg.diff(varDiff))
        )),
        ...args
    );
}

Sumsq.prototype = Object.create(Operation.prototype);
function createFunction(n, funct) {
    function FunctionN(...args) {
        funct.call(this, n, ...args);
    }

    FunctionN.prototype = Object.create(funct.prototype);
    return FunctionN;
}

const [Sumsq2, Sumsq3, Sumsq4, Sumsq5] = [2, 3, 4, 5].map(n => createFunction(n, Sumsq));

function Distance(n, ...args) {
    Operation.call(
        this,
        'distance' + n,
        (...args) => Math.sqrt(sumsqArgs(...args)),
        (varDiff, ...args) => new Divide(
            new Sumsq(n, ...args).diff(varDiff),
            new Multiply(CONST['2'], this)
        ),
        ...args
    );
}

Distance.prototype = Object.create(Operation.prototype);
const [Distance2, Distance3, Distance4, Distance5] = [2, 3, 4, 5].map(n => createFunction(n, Distance));


function SumexpArgs(...args) {
    return args.reduce((acc, arg) => acc + Math.exp(arg), 0);
}

function Sumexp(...args) {
    Operation.call(
        this,
        'sumexp',
        (...args) => SumexpArgs(...args),
        (varDiff, ...args) => new Sum(args.length, ...args.map(arg =>
            new Multiply(arg.diff(varDiff), new Sumexp(arg)))
        ),
        ...args
    )
}

Sumexp.prototype = Object.create(Operation.prototype);

function LSE(...args) {
    Operation.call(
        this,
        'lse',
        (...args) => Math.log(SumexpArgs(...args)),
        (varDiff, ...args) => {
            let sum = new Sumexp(...args);
            return new Divide(sum.diff(varDiff), sum);
        },
        ...args
    )
}

LSE.prototype = Object.create(Operation.prototype);

const OPERATION = {
    "negate": [Negate, 1],
    '+': [Add, 2],
    '-': [Subtract, 2],
    '*': [Multiply, 2],
    '/': [Divide, 2],
    'sumsq2': [Sumsq2, 2],
    'sumsq3': [Sumsq3, 3],
    'sumsq4': [Sumsq4, 4],
    'sumsq5': [Sumsq5, 5],
    'distance2': [Distance2, 2],
    'distance3': [Distance3, 3],
    'distance4': [Distance4, 4],
    'distance5': [Distance5, 5],
    'sumexp': [Sumexp, Infinity],
    'lse': [LSE, Infinity]
};

function ErrorParser(message) {
    this.message = message;
}

ErrorParser.prototype = Object.create(Error.prototype);
ErrorParser.prototype.name = 'ErrorParser';
ErrorParser.prototype.constructor = ErrorParser;

function errorCreator(name, message) {
    const error = function (...args) {
        ErrorParser.call(this, message(...args));
    }
    error.prototype = Object.create(ErrorParser.prototype);
    error.prototype.name = name;
    error.prototype.constructor = error;
    return error;
}

const UnexpectedToken = errorCreator('UnexpectedToken', (position, token) => 'Unexpected token \''
    + token + '\' at' + 'position: ' + position);
const NonCorrectBracketSequence = errorCreator('NonCorrectBracketSequence', (expression, token) =>
    'Non-correct bracket sequence. Expected: \'' + token + '\'' + ' at position: "' + expression + '".');
const NonOperationInBracket = errorCreator('NonOperationInBracket', (array) =>
    'Non operation in bracket: (' + array.join(' ') + ').')
const UnexpectedArgumentsInBracket = errorCreator('UnexpectedArgumentsInBracket', (expression) =>
    'Unexpected number of arguments for operation in bracket: ' + expression)
const EmptyBracket = errorCreator('EmptyBracket', position => "Empty bracket at position " + position)
const EmptyExpression = errorCreator('EmptyExpression', () => 'Empty expression')
const NonArgumentsInBracket = errorCreator('NonArgumentsInBracket', (bracket) => 'Non arguments in bracket: (' + bracket.join(' ') + ').')
const parseBase = (acc, token, modification) => {
    if (token in VARIABLES) {
        acc.push(new Variable(token));
    } else if (token instanceof Array) {
        acc.push(abstractParse(token, modification));
    } else if (token in OPERATION) {
        let elems = acc.splice(-OPERATION[token][1]);
        if (modification === 'prefix') {
            elems = elems.reverse();
        }
        acc.push(new OPERATION[token][0](...elems));
    } else {
        acc.push(new Const(token));
    }
    return acc;
};


const abstractParse = (expression, modification) => {

    let tokens = tokenize(expression, modification);
    let operationToken;
    if (modification === 'base' || modification === 'postfix') {
        operationToken = tokens[tokens.length - 1];
    } else if (modification === 'prefix') {
        operationToken = tokens[0];
    }
    const expectedTokenCount = operationToken in OPERATION ? OPERATION[operationToken][1] : 0;
    if (operationToken in OPERATION && (tokens.length - 1 !== expectedTokenCount && Infinity !== expectedTokenCount) && modification !== 'base') {
        throw new UnexpectedArgumentsInBracket(`(${tokens.join(' ')})`);
    }
    if (/^\s*$/.test(expression)) {
        throw new EmptyExpression();
    }
    if (!(operationToken in OPERATION) && tokens.length !== 1) {
        throw new NonOperationInBracket(tokens);
    }
    const reduceFunction = (acc, token) => parseBase(acc, token, modification);
    // :NOTE: много одинакового кода для префикса и постфикса, а предыдущую домашку можно сюда вовсе не приплетать
    // и твоя токенизация это уже по факту парсинг, можно объединить
    if (modification === 'base' || modification === 'postfix') {
        tokens = tokens.reduce(reduceFunction, []);
    }

    if (modification === 'prefix') {
        tokens = tokens.reduceRight(reduceFunction, []);
    }

    return tokens.pop();
};


const parse = (expression) => abstractParse(expression, "base");
const parsePrefix = (expression) => abstractParse(expression, 'prefix');
const parsePostfix = (expression) => abstractParse(expression, 'postfix');

// :NOTE: в js например корректное число это 1e-9 и 100_000
// воспользуйся стандартной функцией и не мучайся
// +
const isDigit = (token) => !isNaN(token)

function tokenize(input, modification) {
    let tokens = [];
    let index = 0
    let countBracket = 0;
    let firstSymbol;
    let indexFirst;
    let indexLast;
    let lastSymbol;
    let countOpenBracket = 0;
    let countOperation = 0;
    let startIndex = -1;

    const skipWhitespace = () => {
        while (index < input.length && input[index] === ' ') {
            index++;
        }
    }
    const checkBracket = (token) => {
        if (token === '(') {
            countOpenBracket++;
            countBracket++;
        } else if (token === ')') {
            countBracket--;
            if (countBracket < 0) {
                throw new NonCorrectBracketSequence(startIndex === -1 ? 0 : startIndex, '(');
            }
            if (token === ')') {
                startIndex = index;
            }
        }
    }
    const next = () => {
        let token = input[index];
        checkBracket(token);
        index++;
    }

    const parseBracket = (tokenBracket, tokenParent) => {
        skipWhitespace();
        if (input[index] === ')') {
            throw new EmptyBracket(index - 1);
        }
        while (index < input.length) {
            skipWhitespace();
            if (input[index] === '(') {
                next();
                let childToken = [];
                parseBracket(childToken, tokenBracket)
            }
            if (input[index] === ')') {
                tokenParent.push(tokenBracket);
                next();
                return;
            }

            parseToken(tokenBracket, tokens);
        }
    }

    const parseToken = (tokenStack) => {
        let token = input[index];
        checkBracket(token);
        if (token === '(') {
            index++;
            let childTokenBracket = [];
            parseBracket(childTokenBracket, tokenStack);
            return;
        }
        let indexStart = index;
        token = getToken(token);

        skipWhitespace();
        if (token === '') {
            return;
        }
        if (isDigit(token)) {
            tokenStack.push(parseFloat(token));
        } else if (token in VARIABLES || token in OPERATION) {
            if (token in OPERATION) {
                countOperation++;
            }
            tokenStack.push(token);
        } else {
            throw new UnexpectedToken(indexStart, token);
        }
    }

    const getToken = (token) => {
        let currentToken = '';
        while (index < input.length && token !== ' ' && token !== ')' && token !== '(') {
            currentToken += token;
            token = input[++index];
        }
        return currentToken;
    }

    if (input instanceof Array) {
        return input;
    }

    if (modification === 'base') {
        return input.split(' ').filter(token => token.length > 0).map(arg => isDigit(arg) ? parseFloat(arg) : arg);
    }

    skipWhitespace();
    firstSymbol = input[index];
    indexFirst = index;
    for (index; index < input.length; index++) {
        skipWhitespace();
        if (input[index] === '(') {
            next();
            let tokenBracket = [];
            parseBracket(tokenBracket, tokens);
        } else {
            parseToken(tokens);
        }
        checkBracket(input[index])
        lastSymbol = input[index - 1];
        indexLast = index;

    }
    if (tokens.length > 1 && (firstSymbol !== '(' || lastSymbol !== ')') && modification !== 'base') {
        if (firstSymbol !== '(') {
            throw new UnexpectedToken(indexFirst, firstSymbol)
        } else if (lastSymbol !== ')') {
            throw new UnexpectedToken(indexLast, lastSymbol)
        }
    }
    if (countBracket !== 0) {
        throw new NonCorrectBracketSequence(input.length - 1, ')');
    }
    if (countOpenBracket !== countOperation) {
        if (modification === 'prefix' && !(tokens[0][0] in OPERATION)) {
            throw new NonArgumentsInBracket(tokens[0]);
        }
        throw new UnexpectedArgumentsInBracket(input);
    }

    if (firstSymbol === '(') {
        if (tokens[0].length === 1 && OPERATION[tokens[0]][1] !== Infinity) {
            throw new NonArgumentsInBracket(tokens[0]);
        }
        return tokens[0];
    }
    return tokens;
}

console.log(parse("x negate 2 /"))
// :NOTE: Const op (2 args)        : org.graalvm.polyglot.PolyglotException: error: Unexpected number of arguments for operation in bracket: (0 1 2)
// какое здесь количество аргументов ожидается? нужно сказать, что нет операции
// нужно выводить позицию, а не все выражение