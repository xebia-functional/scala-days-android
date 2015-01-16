package com.fortysevendeg.android.scaladays.utils

import java.io.{InputStream, File}

import macroid.AppContext

import scala.util.Try

object FileUtils {
  
  def getAssetsJson(fileName: String) (implicit appContext: AppContext): Try[String] = {
    Try {
      val assetsFile: InputStream = appContext.get.getResources.getAssets.open(fileName)
      scala.io.Source.fromInputStream(assetsFile).mkString
    }
  }
  
  def getCacheJson(fileName: String) (implicit appContext: AppContext): Try[String] = {
    Try {
      val cacheDir: File = appContext.get.getCacheDir
      scala.io.Source.fromFile(new File(cacheDir, fileName), "UTF-8").mkString
    }
  }

}
