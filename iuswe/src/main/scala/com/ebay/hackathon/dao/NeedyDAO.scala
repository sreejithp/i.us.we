package com.ebay.hackathon.dao

import com.ebay.hackathon.{Logging, DB}
import com.ebay.hackathon.entity.{Needy, User}
import org.bson.types.ObjectId

/**
 * Author sreejith on 09/11/14 6:24 AM.
 */
object NeedyDAO extends BaseDAO[Needy, ObjectId](collection = DB.connection("needy"), entityClass = classOf[Needy])
with Logging {

    def createNeedy(needy:Needy): Unit ={
      insert(needy)
    }


}
