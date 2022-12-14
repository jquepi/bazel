// Copyright 2022 The Bazel Authors. All rights reserved.
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
package com.google.devtools.build.lib.skyframe.serialization;

import com.google.common.collect.ImmutableList;
import com.google.devtools.build.lib.skyframe.DirectoryListingStateValue;
import com.google.devtools.build.lib.skyframe.DirectoryListingValue;
import com.google.devtools.build.lib.skyframe.DirectoryListingValue.DifferentRealPathDirectoryListingValue;
import com.google.devtools.build.lib.skyframe.DirectoryListingValue.RegularDirectoryListingValue;
import com.google.devtools.build.lib.skyframe.serialization.testutils.FsUtils;
import com.google.devtools.build.lib.skyframe.serialization.testutils.SerializationTester;
import com.google.devtools.build.lib.vfs.Dirent;
import com.google.devtools.build.lib.vfs.PathFragment;
import com.google.devtools.build.lib.vfs.Root;
import com.google.devtools.build.lib.vfs.RootedPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Tests for {@link DirectoryListingValue}. */
@RunWith(JUnit4.class)
public class DirectoryListingValueCodecTest {

  @Test
  public void testCodec() throws Exception {
    SerializationTester serializationTester =
        new SerializationTester(
            new RegularDirectoryListingValue(
                DirectoryListingStateValue.create(ImmutableList.<Dirent>of())),
            new RegularDirectoryListingValue(
                DirectoryListingStateValue.create(
                    ImmutableList.of(
                        new Dirent("a", Dirent.Type.DIRECTORY),
                        new Dirent("b", Dirent.Type.SYMLINK)))),
            new DifferentRealPathDirectoryListingValue(
                rootedPath("/foo", "bar"),
                DirectoryListingStateValue.create(
                    ImmutableList.of(
                        new Dirent("c", Dirent.Type.UNKNOWN), new Dirent("d", Dirent.Type.FILE)))));
    FsUtils.addDependencies(serializationTester);
    serializationTester.runTests();
  }

  private static RootedPath rootedPath(String root, String relativePath) {
    return RootedPath.toRootedPath(
        Root.fromPath(FsUtils.TEST_FILESYSTEM.getPath(root)), PathFragment.create(relativePath));
  }
}
