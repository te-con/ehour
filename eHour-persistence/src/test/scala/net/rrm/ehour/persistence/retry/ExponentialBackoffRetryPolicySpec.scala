package net.rrm.ehour.persistence.retry

import net.rrm.ehour.AbstractSpec
import org.hibernate.HibernateException

class ExponentialBackoffRetryPolicySpec extends AbstractSpec {
  "Exponential Backoff Retry Policy" should {
    "execute with success" in {
      def success() = true

      ExponentialBackoffRetryPolicy.retry(success) should be (true)
    }

    "retry once" in {
      var attempts = 0

      def success() = {
        if (attempts == 1) {
          true
        } else {
          attempts = attempts + 1
          throw new HibernateException("fail")
        }
      }

      ExponentialBackoffRetryPolicy.retry(success) should be (true)
      attempts should be (1)
    }

    "fail after 2 times" in {
      def success() = throw new HibernateException("fail")

      try {
        ExponentialBackoffRetryPolicy.retry(success, 2)
        fail()
      } catch {
        case e: RuntimeException =>
      }
    }
  }
}
