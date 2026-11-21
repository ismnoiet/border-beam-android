import com.repgem.borderbeam.*

BorderBeam(BeamStyle.small, shape = BeamShape.circle) { StopIcon() }
BorderBeam(BeamStyle.line, shape = BeamShape.capsule) { SearchField() }   // sweeps along the bottom edge
BorderBeam(direction = BeamDirection.rightToLeft, intensity = 0.75) { /* ... */ }
BorderBeam(colors = BeamPalette.single(Color.Cyan)) { /* ... */ }         // one color

// Only the beam, on a composable you style yourself:
MyCard(Modifier.borderBeam(shape = BeamShape.rounded(12)))
