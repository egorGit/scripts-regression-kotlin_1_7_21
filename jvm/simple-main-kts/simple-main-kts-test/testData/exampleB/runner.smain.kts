@file:Import(
  "dependant_return_Any_with_result.smain.kts"
)

val mainFun: () -> Any = {
  callDependantA()
}

mainFun








