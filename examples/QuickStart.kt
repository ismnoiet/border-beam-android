import com.repgem.borderbeam.*

@Composable
fun Prompt() {
    var text by remember { mutableStateOf("") }

    BorderBeam(
        shape = BeamShape.rounded(26),
        colors = BeamPalette.spectrum,
        modifier = Modifier.width(320.dp),
    ) {
        BasicTextField(text, { text = it }, Modifier.padding(16.dp))
    }
}
