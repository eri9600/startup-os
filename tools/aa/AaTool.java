/*
 * Copyright 2018 The StartupOS Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.startupos.tools.aa;

import com.google.startupos.common.CommonModule;
import com.google.startupos.tools.aa.commands.AaCommand;
import com.google.startupos.tools.aa.commands.AddRepoCommand;
import com.google.startupos.tools.aa.commands.DiffCommand;
import com.google.startupos.tools.aa.commands.FixCommand;
import com.google.startupos.tools.aa.commands.InitCommand;
import com.google.startupos.tools.aa.commands.PatchCommand;
import com.google.startupos.tools.aa.commands.ReviewCommand;
import com.google.startupos.tools.aa.commands.SnapshotCommand;
import com.google.startupos.tools.aa.commands.SubmitCommand;
import com.google.startupos.tools.aa.commands.SyncCommand;
import com.google.startupos.tools.aa.commands.WorkspaceCommand;
import dagger.Component;
import dagger.Lazy;
import java.util.HashMap;
import javax.inject.Inject;
import javax.inject.Singleton;

/** aa tool. */
@Singleton
public class AaTool {
  private HashMap<String, Lazy<? extends AaCommand>> commands = new HashMap<>();

  @Inject
  AaTool(
      Lazy<InitCommand> initCommand,
      Lazy<WorkspaceCommand> workspaceCommand,
      Lazy<SyncCommand> syncCommand,
      Lazy<DiffCommand> diffCommand,
      Lazy<FixCommand> fixCommand,
      Lazy<ReviewCommand> reviewCommand,
      Lazy<SnapshotCommand> snapshotCommand,
      Lazy<SubmitCommand> submitCommand,
      Lazy<AddRepoCommand> addRepoCommand,
      Lazy<PatchCommand> patchCommand) {
    commands.put("init", initCommand);
    commands.put("workspace", workspaceCommand);
    commands.put("sync", syncCommand);
    commands.put("diff", diffCommand);
    commands.put("fix", fixCommand);
    commands.put("review", reviewCommand);
    commands.put("snapshot", snapshotCommand);
    commands.put("submit", submitCommand);
    commands.put("add_repo", addRepoCommand);
    commands.put("patch", patchCommand);
  }

  private void printUsage() {
    System.out.println(
        String.format(
            "Invalid usage. Available commands are: %s", String.join(", ", commands.keySet())));
  }

  @Singleton
  @Component(modules = {CommonModule.class, AaModule.class})
  public interface AaToolComponent {
    AaTool getAaTool();
  }

  private boolean run(String[] args) {
    if (args.length > 0) {
      String command = args[0];
      if (commands.containsKey(command)) {
        return commands.get(command).get().run(args);
      } else {
        System.out.println();
        printUsage();
      }
    } else {
      printUsage();
    }
    return false;
  }

  public static void main(String[] args) {
    if (!DaggerAaTool_AaToolComponent.create().getAaTool().run(args)) {
      System.exit(1);
    }
  }
}
