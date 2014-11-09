package com.ebay.hackathon.endpoints


import com.ebay.hackathon.controllers.DeliveryController
import com.ebay.hackathon.dao.FileStore
import org.scalatra.servlet.FileUploadSupport

/**
 * Author sreejith on 09/11/14 6:51 AM.
 */
class DeliveryEndPoint extends ApiEndpoint with HttpSessionSupport with FileUploadSupport {

  private[this] def controller = {
    new DeliveryController(signedInUser)
  }

  get("/needy/:needyId/volunteer/volunteerId/") {

    val needyId = requiredParam("needyId")
    val volunteerId = requiredParam("volunteerId")
    val name = param("name")
    val address = param("address")
    val comments = (if (params.contains("comments")) params("comments").split(",") else Nil).asInstanceOf[List[String]]
    val fileId = fileParams.get("file") match {
      case Some(file) =>
        val fileBytes = scala.io.Source.fromInputStream(file.getInputStream)(io.Codec.ISO8859)
        val fileBytesArray = fileBytes.map(_.toByte).toArray
        FileStore.saveFile(fileBytesArray)
      case None => null
    }
    controller.addDeliveryInfo(name,
      address,
      comments,
      volunteerId,
      needyId,
      List(fileId))

  }

  get("/contributorDeliveryInfo/:contributorId") {
    controller.getContributorDeliveryInfo(requiredParam("contributorId"))
  }

  get("/volunteerDeliveryInfo/:contributorId") {
    controller.getVolunteerDeliveryInfo(requiredParam("contributorId"))
  }

  get("/deliveryInfoBasedOnDate/:date") {
    controller.getDeliveryInfoBasedOnDate(requiredDateParam("date"))
  }

  get("/deliveryPic/:fileId") {
    contentType = "image/png"
    FileStore.getFile(requiredParam("fileId"))
  }


}
