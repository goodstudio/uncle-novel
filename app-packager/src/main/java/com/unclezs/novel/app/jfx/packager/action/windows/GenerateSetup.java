package com.unclezs.novel.app.jfx.packager.action.windows;

import com.unclezs.novel.app.jfx.packager.model.Registry;
import com.unclezs.novel.app.jfx.packager.packager.AbstractPackager;
import com.unclezs.novel.app.jfx.packager.packager.WindowsPackager;
import com.unclezs.novel.app.jfx.packager.util.CommandUtils;
import com.unclezs.novel.app.jfx.packager.util.FileUtils;
import com.unclezs.novel.app.jfx.packager.util.VelocityUtils;
import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 * Creates a Setup file including all app folder's content only for Windows so app could be easily
 * distributed
 */
public class GenerateSetup extends WindowsArtifactGenerator {

  public GenerateSetup() {
    super("Setup installer");
  }

  @Override
  public boolean skip(AbstractPackager packager) {
    return !packager.getWinConfig().isGenerateSetup();
  }

  @Override
  protected File doApply(AbstractPackager packager) throws Exception {
    WindowsPackager windowsPackager = (WindowsPackager) packager;

    File iconFile = windowsPackager.getPlatform().getPlatformConfig().getIconFile();
    File assetsFolder = windowsPackager.getAssetsFolder();
    String name = windowsPackager.getName();
    File outputDirectory = windowsPackager.getOutputDir();
    String version = windowsPackager.getVersion();
    Registry registry = windowsPackager.getWinConfig().getRegistry();

    // checks if registry entries' names are not empy
    if (registry.getEntries().stream()
        .anyMatch(e -> StringUtils.isBlank(e.getKey()) || StringUtils.isBlank(e.getValueName()))) {
      throw new Exception("One or more registry entries have no key and/or value name");
    }

    // copies ico file to assets folder
    FileUtils.copyFileToFolder(iconFile, assetsFolder);

    // generates iss file from velocity template
    File issFile = new File(assetsFolder, name + ".iss");
    VelocityUtils.render("windows/iss.vm", issFile, windowsPackager);

    // generates windows installer with inno setup command line compiler
    CommandUtils
        .execute("iscc", "/O" + outputDirectory.getAbsolutePath(), "/F" + name + "_" + version,
            issFile);

    // setup file
    File setupFile = new File(outputDirectory, name + "_" + version + ".exe");
    if (!setupFile.exists()) {
      throw new Exception("Windows setup file generation failed!");
    }

    // sign installer
    sign(setupFile, windowsPackager);

    return setupFile;
  }

}
