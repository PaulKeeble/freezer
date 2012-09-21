package freezer.serialisers
import org.scalatest.matchers.ShouldMatchers

trait NewRoundTrip extends ShouldMatchers {
  
  def roundTrip[@specialized T](serialiser: NewSerialiser[T], deserialiser: NewDeserialiser[T], value: T) {
    val packed = serialiser(value)
    val result = deserialiser(packed)
    result._1 should equal(value)
    result._2 === Array()
  }

  def roundTrip[@specialized T](value: T)(implicit serialiser: NewSerialiser[T], deserialiser: NewDeserialiser[T]) {
    roundTrip(serialiser, deserialiser, value)
  }
  
  def roundTripAll[U, T](values: List[U], f: (U) => T)(implicit serialiser: NewSerialiser[T], deserialiser: NewDeserialiser[T]) {
    roundTripAll( values.map(f(_)) )
  }
  
  def roundTripAll[T](values:List[T])(implicit serialiser: NewSerialiser[T], deserialiser: NewDeserialiser[T]) {
    values.foreach {
      v => roundTrip(serialiser,deserialiser,v)
    }
  }
}