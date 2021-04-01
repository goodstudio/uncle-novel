package com.unclezs.novel.app.jfx.packager.action.mac;

import com.unclezs.novel.app.jfx.packager.action.ArtifactGenerator;
import com.unclezs.novel.app.jfx.packager.packager.AbstractPackager;
import com.unclezs.novel.app.jfx.packager.packager.MacPackager;
import com.unclezs.novel.app.jfx.packager.util.CommandUtils;
import java.io.File;

/**
 * 创建一个PKG安装程序文件，其中包含所有应用程序文件夹的内容，仅适用于MacOS，这样应用程序就可以轻松分发
 *
 * @author https://github.com/fvarrui/JavaPackager
 * @author blog.unclezs.com
 * @since 2021/03/23 19:10
 */
public class GeneratePkg extends ArtifactGenerator {

  public GeneratePkg() {
    super("PKG installer");
  }

  @Override
  public boolean skip(AbstractPackager packager) {
    return !packager.getMacConfig().isGeneratePkg();
  }

  @Override
  protected File doApply(AbstractPackager packager) throws Exception {
    MacPackager macPackager = (MacPackager) packager;

    File appFile = macPackager.getAppFile();
    String name = macPackager.getName();
    File outputDirectory = macPackager.getOutputDir();
    String version = macPackager.getVersion();

    File pkgFile = new File(outputDirectory, name + "_" + version + ".pkg");

    // invokes pkgbuild command
    CommandUtils.execute("pkgbuild", "--install-location", "/Applications", "--component", appFile,
        pkgFile);

    // checks if pkg file was created
    if (!pkgFile.exists()) {
      throw new Exception(getArtifactName() + " generation failed!");
    }

    return pkgFile;
  }

}
