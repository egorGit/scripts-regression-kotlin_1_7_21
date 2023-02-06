@file:Import(
  "model.smain.kts",
  "dependant_return_Model_no_result.smain.kts"
)

val mainFun: () -> SomeDummyWrapper = {
  callDependantA()
}

mainFun








