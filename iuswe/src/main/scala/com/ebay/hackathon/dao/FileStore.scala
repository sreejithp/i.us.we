package com.ebay.hackathon.dao

import java.util.UUID

import com.ebay.hackathon.DB

/**
 * Author sreejith on 09/11/14 6:57 AM.
 */
object FileStore {

  def saveFile(byteArray: Array[Byte]): String = {
    val fileId = UUID.randomUUID().toString
    // Save a file to GridFS
    val id = DB.gridFS(byteArray) { f =>
      f.filename = fileId
      f.contentType = "image/png"
    }
    fileId
  }

  def getFile(fileId: String): Option[Array[Byte]] = {
    val myFile = DB.gridFS.findOne(fileId)
    myFile match {
      case Some(file) => Option(Stream.continually(file.inputStream.read).takeWhile(-1 !=).map(_.toByte).toArray)
      case None => None
    }
  }

}
