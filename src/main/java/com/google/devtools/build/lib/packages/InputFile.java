// Copyright 2014 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.packages;

import com.google.common.base.Preconditions;
import com.google.devtools.build.lib.cmdline.Label;
import com.google.devtools.build.lib.concurrent.ThreadSafety.Immutable;
import com.google.devtools.build.lib.concurrent.ThreadSafety.ThreadSafe;
import com.google.devtools.build.lib.vfs.Path;
import com.google.devtools.build.lib.vfs.PathFragment;
import javax.annotation.Nullable;
import net.starlark.java.syntax.Location;

/**
 * A file that is an input to the build system.
 *
 * <p>In the build system, a file is considered an <i>input</i> file iff it is
 * not generated by the build system (e.g. it's maintained under version
 * control, or created by the test harness).  It has nothing to do with the
 * type of the file; a generated file containing <code>Java</code> source code
 * is an OutputFile, not an InputFile.
 */
@Immutable @ThreadSafe
public final class InputFile extends FileTarget {
  private final Package pkg;
  private final Location location;
  private final RuleVisibility visibility;
  private final License license;

  /**
   * Constructs an input file with the given label, which must be a label for
   * the given package, and package-default visibility.
   */
  InputFile(Package pkg, Label label, Location location) {
    this(pkg, label, location, /*visibility=*/ null, License.NO_LICENSE);
  }

  /**
   * Constructs an input file with the given label, which must be a label for the given package
   * that was parsed from the specified location, and has the specified visibility.
   */
  InputFile(Package pkg, Label label, Location location, RuleVisibility visibility,
      License license) {
    super(pkg, label);
    this.pkg = pkg;
    this.location = Preconditions.checkNotNull(location);
    this.visibility = visibility;
    this.license = license;
  }

  @Override
  public Package getPackage() {
    return pkg;
  }

  public boolean isVisibilitySpecified() {
    return visibility != null;
  }

  @Override
  public RuleVisibility getVisibility() {
    if (visibility != null) {
      return visibility;
    } else {
      return pkg.getDefaultVisibility();
    }
  }

  @Override
  public boolean isConfigurable() {
    return false;
  }

  public boolean isLicenseSpecified() {
    return license != null && license.isSpecified();
  }

  @Override
  public License getLicense() {
    if (license != null) {
      return license;
    } else {
      return pkg.getDefaultLicense();
    }
  }

  /**
   * Returns the path to the location of the input file (which is necessarily
   * within the source tree, not beneath <code>bin</code> or
   * <code>genfiles</code>.
   *
   * <p>Prefer {@link #getExecPath} if possible.
   */
  public Path getPath() {
    return pkg.getPackageDirectory().getRelative(label.getName());
  }

  /**
   * Returns the exec path of the file, i.e. the path relative to the execution root working
   * directory.
   */
  public PathFragment getExecPath(boolean siblingRepositoryLayout) {
    return label
        .getRepository()
        .getExecPath(siblingRepositoryLayout)
        .getRelative(label.getPackageName())
        .getRelative(label.getName());
  }

  @Override
  public String getTargetKind() {
    return targetKind();
  }

  @Override
  @Nullable
  public Rule getAssociatedRule() {
    return null;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  /** Returns the target kind for all input files. */
  public static String targetKind() {
    return "source file";
  }
}
