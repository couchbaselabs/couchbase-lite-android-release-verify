#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?Usage: $0 <version> <coreArtifact> <ktxArtifact>}"
CORE_ARTIFACT="${2:?Usage: $0 <version> <coreArtifact> <ktxArtifact>}"
KTX_ARTIFACT="${3:?Usage: $0 <version> <coreArtifact> <ktxArtifact>}"

SCRIPT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR/tests"
chmod +x ./gradlew

./gradlew --no-daemon \
  -PcblVersion="$VERSION" \
  -PcblCoreArtifact="$CORE_ARTIFACT" \
  -PcblKtxArtifact="$KTX_ARTIFACT" \
  :app:connectedCoreDebugAndroidTest

./gradlew --no-daemon \
  -PcblVersion="$VERSION" \
  -PcblCoreArtifact="$CORE_ARTIFACT" \
  -PcblKtxArtifact="$KTX_ARTIFACT" \
  :app:connectedKtxDebugAndroidTest