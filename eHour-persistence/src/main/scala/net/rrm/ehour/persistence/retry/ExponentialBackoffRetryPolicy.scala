package net.rrm.ehour.persistence.retry

import java.util.Random

import org.apache.log4j.Logger
import org.hibernate.HibernateException

object ExponentialBackoffRetryPolicy {
  private final val MaxAttempts = 5
  private final val Log = Logger.getLogger(ExponentialBackoffRetryPolicy.getClass)

  def retry[T](op: => T, maxAttempts:Int = MaxAttempts) = {
    def execOrWait(waitTimes: Seq[Int], lastException: Option[HibernateException]): T = {
      if (waitTimes.nonEmpty) {
        try {
          op
        } catch {
          case e: HibernateException =>
            val waitTime = waitTimes.head
            val attempt = 1 + (maxAttempts - waitTimes.length)
            Log.warn(s"Attempt $attempt: Failed to query, sleeping for $waitTime ms", e)
            Thread.sleep(waitTime)
            execOrWait(waitTimes.tail, Some(e))
        }
      } else {
        lastException match {
          case Some(e) =>
            Log.warn("Giving up on query, max. attempts reached")
            throw new RuntimeException(e)
          case None => throw new RuntimeException("No waittimes found")
        }
      }
    }

    val random = new Random()
    val waitTimes = for (i <-  1 to maxAttempts) yield { 1000 * Math.max(1, random.nextInt(1 << i)) }

    execOrWait(waitTimes, None)
  }
}
