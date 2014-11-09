package com.ebay.hackathon.controllers

import com.ebay.hackathon.dao.NeedyDAO
import com.ebay.hackathon.endpoints.BaseController
import com.ebay.hackathon.entity.Needy
import org.bson.types.ObjectId

/**
 * Author sreejith on 09/11/14 6:41 AM.
 */
class NeedyController(signedInUser: String) extends BaseController(signedInUser) {

  def createNeedy(name: String,
                  address: String,
                  totalPeople: Int,
                  lat: Double,
                  lng: Double) = {
    respond("createNeedy") {
      val needy = new Needy()
      needy.name = name
      needy.address = address
      needy.totalPeople = totalPeople
      needy.loc = List(lat, lng)
      NeedyDAO.createNeedy(needy)
    }
  }

  def getNeedy = {
    respond("getNeedy") {
      NeedyDAO.getNeedy
    }
  }

  def getNeedyBasedOnAssignment(volId: String) = {
    respond("getNeedyBasedOnAssignment") {
      NeedyDAO.getNeedyBasedOnAssignment(volId)
    }
  }

  def assignVolunteerToNeedy(volId: String, id: String) = {
    respond("assignVolunteerToNeedy") {
      NeedyDAO.assignVolunteerToNeedy(volId, new ObjectId(id))
    }
  }


}
