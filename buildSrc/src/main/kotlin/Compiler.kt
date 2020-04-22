object CompilerArguments {
    const val inlineClasses = "-XXLanguage:+InlineClasses"
    const val coroutines = "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    const val time = "-Xopt-in=kotlin.time.ExperimentalTime"
    const val unstableDefault = "-Xopt-in=kotlinx.serialization.UnstableDefault"
}

object Jvm {
    const val target = "1.8"
}