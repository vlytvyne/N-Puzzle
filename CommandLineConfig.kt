import Heuristic.*

object CommandLineConfig {

	var helpCalled = false
	var verboseOutput = false
	var heuristic = MANHATTAN

	fun parseCommandLineArguments(args: Array<String>){
		helpCalled = args.contains("-h")
		verboseOutput = args.contains("-v")
		args.toList().zipWithNext {
			key, heuristicName -> if (key == "-hr") heuristic = heuristicsMapping.getOrDefault(heuristicName, MANHATTAN)
		}
	}
}

val heuristicsMapping = hashMapOf<String, Heuristic>(
	"1" to HAMMING,
	"2" to MANHATTAN,
	"3" to LINEAR_CONFLICT,
	"hamming" to HAMMING,
	"manhattan" to MANHATTAN,
	"lin-conf" to LINEAR_CONFLICT
)