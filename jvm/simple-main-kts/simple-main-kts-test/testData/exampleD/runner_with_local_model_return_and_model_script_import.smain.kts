@file:Import(
  "model.smain.kts",
  "dependant_return_Model_with_result.smain.kts"
)

data class LocalWrapper(
  val data: Any
)

val mainFun: () -> LocalWrapper = {
  LocalWrapper(callDependantA())
}

mainFun








