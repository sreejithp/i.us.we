package com.ebay.hackathon

import org.slf4j.LoggerFactory

trait Logging {
  @transient lazy val LOGGER = LoggerFactory getLogger getClass.getName
}
