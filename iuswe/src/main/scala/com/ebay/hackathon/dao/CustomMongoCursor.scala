package com.ebay.hackathon.dao

import com.ebay.hackathon.entity.traits.SerialisableEntity
import com.mongodb.casbah.Imports._
import com.mongodb.DBCursor
import com.mongodb.casbah.CursorExplanation

/**Unfortunately, MongoCursorBase is typed to DBObject, but....
 * Ripped off from casbah-mapper.
 * https://github.com/maxaf/casbah-mapper/blob/master/src/main/scala/mapper/MappedCollection.scala
 *
 */

trait CustomMongoCursorBase[T <: SerialisableEntity[T]] {

  val entityClazz: Class[T]

  val underlying: DBCursor

  def next() = entityClazz.newInstance().fromDBObject(underlying.next)

  def hasNext = underlying.hasNext

  def sort[A <% DBObject](orderBy: A): this.type = {
    // The Java code returns a copy of itself (via _this_) so no clone/_newInstance
    if(orderBy!=null)
      underlying.sort(orderBy)
    this
  }

  def count = underlying.count

  def option_=(option: Int): Unit = underlying.addOption(option)

  def option = underlying.getOptions

  def resetOptions() = underlying.resetOptions() // use parens because this side-effects

  def options = underlying.getOptions

  def options_=(opts: Int): Unit = underlying.setOptions(opts)

  def hint[A <% DBObject](indexKeys: A): this.type = {
    underlying.hint(indexKeys)
    this
  }

  def hint(indexName: String): this.type = {
    underlying.hint(indexName)
    this
  }

  def snapshot(): this.type = {
    // The Java code returns a copy of itself (via _this_) so no clone/_newInstance
    underlying.snapshot() // parens for side-effecting
    this
  }

  def explain = new CursorExplanation(underlying.explain)

  def limit(n: Int): this.type = {
    underlying.limit(n)
    this
  }

  def skip(n: Int): this.type = {
    underlying.skip(n)
    this
  }

  def cursorId = underlying.getCursorId()

  def close() = underlying.close() // parens for side-effect

  def slaveOk() = underlying.getReadPreference.isSlaveOk // parens for side-effect

  def numGetMores = underlying.numGetMores

  def numSeen = underlying.numSeen

  def sizes = underlying.getSizes

  def batchSize(n: Int) = {
    underlying.batchSize(n)
    this
  }

  def keysWanted = underlying.getKeysWanted

  def query = underlying.getQuery

  def addSpecial(name: String, o: Any): this.type = {
    // The Java code returns a copy of itself (via _this_) so no clone/_newInstance
    underlying.addSpecial(name, o.asInstanceOf[AnyRef])
    this
  }

  def $returnKey(bool: Boolean = true): this.type = addSpecial("$returnKey", bool)

  def $maxScan[A: Numeric](max: T): this.type = addSpecial("$maxScan", max)

  def $query[A <% DBObject](q: A): this.type = addSpecial("$query", q)

  def $orderby[A <% DBObject](obj: A): this.type = addSpecial("$orderby", obj)

  def $explain(bool: Boolean = true): this.type = addSpecial("$explain", bool)

  def $snapshot(bool: Boolean = true): this.type = addSpecial("$snapshot", bool)

  def $min[A <% DBObject](obj: A): this.type = addSpecial("$min", obj)

  def $max[A <% DBObject](obj: A): this.type = addSpecial("$max", obj)

  def $showDiskLoc(bool: Boolean = true): this.type = addSpecial("$showDiskLoc", bool)

  def $hint[A <% DBObject](obj: A): this.type = addSpecial("$hint", obj)

  def _newInstance(cursor: DBCursor): CustomMongoCursorBase[T]

  def copy(): CustomMongoCursorBase[T] = _newInstance(underlying.copy()) // parens for side-effects
}

case class CustomMongoCursor[T <: SerialisableEntity[T] : Manifest](entityClazz: Class[T], underlying: DBCursor) extends CustomMongoCursorBase[T] with Iterator[T] {

  def _newInstance(cursor: DBCursor) = CustomMongoCursor(entityClazz, cursor)
}