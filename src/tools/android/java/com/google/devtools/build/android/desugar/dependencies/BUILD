# Description:
#   Dependency tracking helper library for desugar that we package separately
#   for the benefit of desugar users who want to compile the tool from source.

load("@rules_java//java:defs.bzl", "java_library")

package(
    default_visibility = [
        "//src/test/java/com/google/devtools/build/android/desugar:__subpackages__",
        "//src/tools/android/java/com/google/devtools/build/android:__pkg__",
        "//src/tools/android/java/com/google/devtools/build/android/desugar:__pkg__",
        "//src/tools/android/java/com/google/devtools/build/android/r8:__pkg__",
    ],
)

java_library(
    name = "dependencies",
    srcs = glob(["*.java"]),
    deps = [
        "//src/main/protobuf:desugar_deps_java_proto",
        "//src/tools/android/java/com/google/devtools/build/android/r8:deps_collector_api",
        "//third_party:guava",
        "//third_party:jsr305",
    ],
)

filegroup(
    name = "srcs",
    srcs = glob(["**"]),
)
