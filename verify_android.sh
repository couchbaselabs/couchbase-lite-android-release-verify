#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:?Usage: $0 <version>}"

WORK_DIR="$(mktemp -d)"
cleanup() { rm -rf -- "$WORK_DIR"; }
trap cleanup EXIT

OUT_DIR="${WORK_DIR}/cbl-downloads"

CENTRAL_BASE="https://repo1.maven.org/maven2"
EE_BASE="https://mobile.maven.couchbase.com/maven2/dev"
GROUP_PATH="com/couchbase/lite"

COMMUNITY=("couchbase-lite-android" "couchbase-lite-android-ktx")
ENTERPRISE=("couchbase-lite-android-ee" "couchbase-lite-android-ee-ktx")

download_one() {
  local repo="$1" artifact="$2" version="$3" dest="$4"
  local base="${repo}/${GROUP_PATH}/${artifact}/${version}"
  local aar="${artifact}-${version}.aar"
  local pom="${artifact}-${version}.pom"

  mkdir -p "$dest"

  curl -fL --retry 3 --retry-delay 2 --show-error -o "${dest}/${aar}" "${base}/${aar}"
  curl -fL --retry 3 --retry-delay 2 --show-error -o "${dest}/${pom}" "${base}/${pom}"

  test -s "${dest}/${aar}"
  test -s "${dest}/${pom}"
}

for a in "${COMMUNITY[@]}"; do
  download_one "$CENTRAL_BASE" "$a" "$VERSION" "${OUT_DIR}/community"
done

for a in "${ENTERPRISE[@]}"; do
  download_one "$EE_BASE" "$a" "$VERSION" "${OUT_DIR}/enterprise"
done

echo "OK: all artifacts downloaded for version ${VERSION} (temp dir will be deleted): ${OUT_DIR}"
