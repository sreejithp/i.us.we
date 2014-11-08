package com.ebay.hackathon.endpoints

import org.json4s.{JField, FieldSerializer, DefaultFormats, Formats}
import com.odela.entity._
import course._
import com.odela.api.RelatedWords
import com.odela.entity.points._
import com.odela.entity.feeds.{NewsFeed, GlobalNewsFeed}


object JsonFomats {
  private[this] def ignoreEmpty: PartialFunction[(String, Any), Option[(String, Any)]] = {
    case (name, x) =>
      if(null == x || (x.isInstanceOf[Seq[_]] && x.asInstanceOf[Seq[_]].length == 0)) None
      else Some((name, x))
  }

  private[this] def fieldSerializer[T](implicit mf: Manifest[T]) = FieldSerializer[T](ignoreEmpty)

  val jsonFormats: Formats = DefaultFormats +
    fieldSerializer[TestData]


}
