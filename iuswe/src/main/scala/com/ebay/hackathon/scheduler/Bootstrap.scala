package com.ebay.hackathon.scheduler

import akka.actor.{ActorSystem, Cancellable}
import com.ebay.hackathon.Logging
import com.ebay.hackathon.jobs.FoodAllocator


/**
 * Created by sreejith on 09/11/14.
 */

class Bootstrap extends Logging {
  private[this] val scheduler = new Scheduler()
  val actorSystem = ActorSystem("iuswe")

  def init() = {
    scheduler.start()
    this
  }

  def destroy() {
    scheduler.stop()
  }

  // Schedules timed/recurring jobs in the app
  private[this] class Scheduler {
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration._

    private[this] val cancellables = new collection.mutable.Stack[Cancellable]

    def start() {
      LOGGER.info("Scheduler.start")
      LOGGER.trace("Scheduling Task Allocation")
      cancellables.push(actorSystem.scheduler.schedule(10 seconds, 24 minute, new FoodAllocator))
    }

    def stop() {
      LOGGER.info("Scheduler.stop")
      while (cancellables.length > 0) cancellables.pop().cancel()
    }
  }

}