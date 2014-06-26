package net.rrm.ehour.persistence.retry

import java.util.Random

import org.apache.log4j.Logger
import org.hibernate.HibernateException

object ExponentialBackoffRetryPolicy {
  private final val MaxAttempts = 5
  private final val LOG = Logger.getLogger(ExponentialBackoffRetryPolicy.getClass)

  def retry(op: () => AnyRef) = {
    def execOrWait(waitTimes: Seq[Int], lastException: Option[HibernateException]): AnyRef = {
      if (waitTimes.nonEmpty) {
        try {
          op()
        } catch {
          case e: HibernateException =>
            val waitTime = waitTimes.head
            val attempt = MaxAttempts - waitTimes.length

            LOG.warn(s"Attempt $attempt Failed to query, sleeping for $waitTime ms0", e)
            Thread.sleep(waitTime)
            execOrWait(waitTimes.tail, Some(e))
        }
      } else {
        lastException match {
          case Some(e) => {
            LOG.warn("Giving up on query, max. attempts reached")
            throw new RuntimeException(e)
          }
          case None => throw new RuntimeException("No waittimes found")
        }
      }
    }

    val random = new Random()
    val waitTimes = for (i <-  1 to MaxAttempts) yield { 1000 * Math.max(1, random.nextInt(1 << i)) }

    execOrWait(waitTimes, None)
  }
}
