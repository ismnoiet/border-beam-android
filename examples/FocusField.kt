import com.repgem.borderbeam.*

@Composable
fun FocusField() {
    var email by remember { mutableStateOf("") }
    var focused by remember { mutableStateOf(false) }

    BorderBeam(
        shape = BeamShape.rounded(16),
        active = focused,
        colors = BeamPalette.aurora,
    ) {
        BasicTextField(
            email, { email = it },
            Modifier.padding(16.dp).onFocusChanged { focused = it.isFocused },
        )
    }
}
