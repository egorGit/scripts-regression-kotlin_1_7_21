@file:Import("model.smain.kts")

val callDependantA: () -> SomeDummyWrapper = {
 SomeDummyWrapper(1)
}

callDependantA
