@file:Import("model.smain.kts")

val callDependantA: () -> Any = {
 SomeDummyWrapper(1)
}

callDependantA
