package com.ebay.hackathon.controllers

import java.util.Date
import javax.servlet.http.HttpSession

import com.ebay.hackathon.dao.DeliveryInfoDAO
import com.ebay.hackathon.endpoints.{BaseController, Response}
import com.ebay.hackathon.entity.DeliveryInfo
import org.joda.time.DateTime

/**
 * Author sreejith on 09/11/14 6:40 AM.
 */
class DeliveryController(signedInUser: String) extends BaseController(signedInUser) {


  def addDeliveryInfo(name: String,
                      address: String,
                      comments: List[String],
                      volunteerId: String,
                      needyId: String,
                      photosIds: List[String])(implicit session: HttpSession): Response = requireSignIn {
    respond("addDeliveryInfo") {

      val deliveryInfo = new DeliveryInfo()
      deliveryInfo.name = name
      deliveryInfo.address = address
      deliveryInfo.comments = comments
      deliveryInfo.needyId = needyId
      deliveryInfo.volunteerId = volunteerId
      deliveryInfo.deliveryDate = DateTime.now()
      deliveryInfo.photosIds = photosIds

      //Add contributors
      DeliveryInfoDAO.addDeliveryInfo(deliveryInfo)
    }
  }

  def getContributorDeliveryInfo(contributorId: String) = {
    respond("getContributorDeliveryInfo") {
      DeliveryInfoDAO.getContributorDeliveryInfo(contributorId)
    }
  }

  def getVolunteerDeliveryInfo(volunteerId: String) = {
    respond("getVolunteerDeliveryInfo") {
      DeliveryInfoDAO.getVolunteerDeliveryInfo(volunteerId)
    }
  }

  def getDeliveryInfoBasedOnDate(date: Date) = {
    respond("getDeliveryInfoBasedOnDate") {
      DeliveryInfoDAO.getDeliveryInfoBasedOnDate(new DateTime(date))
    }
  }


}
