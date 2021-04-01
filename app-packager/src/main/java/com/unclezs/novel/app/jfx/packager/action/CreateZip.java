package com.unclezs.novel.app.jfx.packager.action;

import com.unclezs.novel.app.jfx.packager.Context;
import com.unclezs.novel.app.jfx.packager.packager.AbstractPackager;
import com.unclezs.novel.app.jfx.packager.packager.MacPackager;
import com.unclezs.novel.app.jfx.packager.util.Platform;
import java.io.File;
import java.util.UUID;
import org.gradle.api.tasks.bundling.Zip;

/**
 * Creates zipball (zip file)  on Gradle context
 */
public class CreateZip extends ArtifactGenerator {

  public CreateZip() {
    super("Zipball");
  }

  @Override
  public boolean skip(AbstractPackager packager) {
    return !packager.getCreateZip();
  }

  @Override
  protected File doApply(AbstractPackager packager) throws Exception {
    String name = packager.getName();
    String version = packager.getVersion();
    Platform platform = packager.getPlatform();
    File outputDirectory = packager.getOutputDir();
    File appFolder = packager.getAppFolder();
    File executable = packager.getExecutable();
    String jreDirectoryName = packager.getJreDirName();

    File zipFile = new File(outputDirectory, name + "-" + version + "-" + platform + ".zip");

    Zip zipTask = createZipTask();
    zipTask.setProperty("archiveFileName", zipFile.getName());
    zipTask.setProperty("destinationDirectory", outputDirectory);

    // if zipball is for windows platform
    if (Platform.windows.equals(platform)) {

      zipTask.from(appFolder.getParentFile(), copySpec -> {
        copySpec.include(appFolder.getName() + "/**");
      });

    }

    // if zipball is for linux platform
    else if (Platform.linux.equals(platform)) {

      zipTask.from(appFolder.getParentFile(), copySpec -> {
        copySpec.include(appFolder.getName() + "/**");
        copySpec.exclude(appFolder.getName() + "/" + executable.getName());
        copySpec.exclude(appFolder.getName() + "/" + jreDirectoryName + "/bin/*");
      });
      zipTask.from(appFolder.getParentFile(), copySpec -> {
        copySpec.include(appFolder.getName() + "/" + executable.getName());
        copySpec.include(appFolder.getName() + "/" + jreDirectoryName + "/bin/*");
        copySpec.setFileMode(0755);
      });

    }

    // if zipball is for macos platform
    else if (Platform.mac.equals(platform)) {

      MacPackager macPackager = (MacPackager) packager;
      File appFile = macPackager.getAppFile();

      zipTask.from(appFolder, copySpec -> {
        copySpec.include(appFile.getName() + "/**");
        copySpec.exclude(appFile.getName() + "/Contents/MacOS/" + executable.getName());
        copySpec.exclude(appFile.getName() + "/Contents/MacOS/universalJavaApplicationStub");
        copySpec.exclude(
            appFile.getName() + "/Contents/PlugIns/" + jreDirectoryName + "/Contents/Home/bin/*");

      });
      zipTask.from(appFolder, copySpec -> {
        copySpec.include(appFile.getName() + "/Contents/MacOS/" + executable.getName());
        copySpec.include(appFile.getName() + "/Contents/MacOS/universalJavaApplicationStub");
        copySpec.include(
            appFile.getName() + "/Contents/PlugIns/" + jreDirectoryName + "/Contents/Home/bin/*");
        copySpec.setFileMode(0755);
      });

    }

    zipTask.getActions().forEach(action -> action.execute(zipTask));

    return zipFile;
  }

  private Zip createZipTask() {
    return Context.project.getTasks()
      .create("createZip_" + UUID.randomUUID(), Zip.class);
  }

}
