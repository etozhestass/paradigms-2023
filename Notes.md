Тесты дз-8 не прошли, причем последний коммит мой же от 3 апреля
``` 
Exception in thread "main" java.lang.AssertionError: Error while testing new Const(10): No error expected in 
    in expr.prefix()
    where expr = new Const(10)
: org.graalvm.polyglot.PolyglotException: TypeError: (intermediate value).prefix is not a function
        at jstest.expression.BaseTester.test(Unknown Source)
        at jstest.expression.Builder.lambda$selector$4(Unknown Source)
        at base.Selector$Composite.lambda$v$0(Unknown Source)
        at base.Selector.lambda$test$2(Unknown Source)
        at base.Log.lambda$action$0(Unknown Source)
        at base.Log.silentScope(Unknown Source)
        at base.Log.scope(Unknown Source)
        at base.Log.scope(Unknown Source)
        at base.Selector.lambda$test$3(Unknown Source)
        at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        at base.Selector.test(Unknown Source)
        at base.Selector.main(Unknown Source)
        at jstest.prefix.FullPrefixTest.main(Unknown Source)
        at jstest.prefix.FullPostfixTest.main(Unknown Source)
Caused by: jstest.EngineException: No error expected in 
    in expr.prefix()
    where expr = new Const(10)
: org.graalvm.polyglot.PolyglotException: TypeError: (intermediate value).prefix is not a function
        at jstest.JSEngine.eval(Unknown Source)
        at jstest.JSExpressionEngine.evaluate(Unknown Source)
        at jstest.JSExpressionEngine.toString(Unknown Source)
        at jstest.object.ObjectTester.lambda$test$1(Unknown Source)
        at base.TestCounter.lambda$test$0(Unknown Source)
        at base.TestCounter.lambda$testV$2(Unknown Source)
        at base.Log.silentScope(Unknown Source)
        at base.TestCounter.testV(Unknown Source)
        at base.TestCounter.test(Unknown Source)
        at jstest.object.ObjectTester.test(Unknown Source)
        at jstest.prefix.ParserTester.test(Unknown Source)
        at jstest.expression.BaseTester.lambda$test$1(Unknown Source)
        at base.Log.lambda$action$0(Unknown Source)
        at base.Log.silentScope(Unknown Source)
        at base.Log.scope(Unknown Source)
        at base.Log.scope(Unknown Source)
        at jstest.expression.BaseTester.test(Unknown Source)
        ... 14 more
Caused by: javax.script.ScriptException: org.graalvm.polyglot.PolyglotException: TypeError: (intermediate value).prefix is not a function
        at org.graalvm.js.scriptengine/com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.toScriptException(GraalJSScriptEngine.java:503)
        at org.graalvm.js.scriptengine/com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.eval(GraalJSScriptEngine.java:480)
        at org.graalvm.js.scriptengine/com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.eval(GraalJSScriptEngine.java:446)
        at java.scripting/javax.script.AbstractScriptEngine.eval(AbstractScriptEngine.java:262)
        ... 31 more
Caused by: org.graalvm.polyglot.PolyglotException: TypeError: (intermediate value).prefix is not a function
        at <js>.:program(<eval>:1)
        at org.graalvm.sdk/org.graalvm.polyglot.Context.eval(Context.java:399)
        at org.graalvm.js.scriptengine/com.oracle.truffle.js.scriptengine.GraalJSScriptEngine.eval(GraalJSScriptEngine.java:478)
        ... 33 more
ERROR: Tests: failed
```