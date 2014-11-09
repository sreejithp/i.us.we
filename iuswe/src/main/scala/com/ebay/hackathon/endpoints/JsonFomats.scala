package com.ebay.hackathon.endpoints

import com.ebay.hackathon.entity.{DeliveryInfo, FoodAvailability, Needy, User}
import org.json4s.{DefaultFormats, FieldSerializer, Formats}


object JsonFomats {
  private[this] def ignoreEmpty: PartialFunction[(String, Any), Option[(String, Any)]] = {
    case (name, x) =>
      if (null == x || (x.isInstanceOf[Seq[_]] && x.asInstanceOf[Seq[_]].length == 0)) None
      else Some((name, x))
  }

  private[this] def fieldSerializer[T](implicit mf: Manifest[T]) = FieldSerializer[T](ignoreEmpty)

  val jsonFormats: Formats = DefaultFormats +
    fieldSerializer[User]
  fieldSerializer[Needy]
  fieldSerializer[FoodAvailability]
  fieldSerializer[DeliveryInfo]

}
