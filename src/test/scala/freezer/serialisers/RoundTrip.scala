package freezer.serialisers
import org.scalatest.matchers.ShouldMatchers

trait RoundTrip[T] extends ShouldMatchers {
  val serialiser : Serialiser[T]
  
  def roundTrip(value : T) {
    val stored = serialiser.store(value)
    val loaded = serialiser.load(stored)
    
    loaded.result should equal (value)
    loaded.remaining === Array()
  }
}