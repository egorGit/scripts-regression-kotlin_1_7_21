@file:Import(
  "dependant_return_Any_no_result.smain.kts"
)

val mainFun: () -> Any = {
  callDependantA()
}

mainFun








