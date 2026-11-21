#!/usr/bin/env bash
# Runs everything CI runs, tests first, then the builds. Works on its own, no need for the rest of the repo.
#   ./scripts/check.sh            unit tests, then the release AAR and the demo module
#   ./scripts/check.sh --fast     unit tests only
set -euo pipefail
cd "$(dirname "$0")/.."

./gradlew :borderbeam:testDebugUnitTest
[[ "${1:-}" == "--fast" ]] && exit 0

./gradlew :borderbeam:assembleRelease :borderbeam-demo:assembleDebug
echo "OK: tests pass, AAR and demo build."
