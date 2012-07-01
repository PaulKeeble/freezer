package freezer.serialisers
import org.scalatest.matchers.ShouldMatchers

trait NewRoundTrip extends ShouldMatchers {
  
  def roundTrip[@specialized T](serialiser: NewSerialiser[T], deserialiser: NewDeserialiser[T], value: T) {
    val typeName = typeOf(value)
    
    val packed = serialiser(value)
    val result = deserialiser(packed)
    result._1 should equal(value)
    result._2 === Array()
  }

  def roundTrip[@specialized T](value: T)(implicit serialiser: NewSerialiser[T], deserialiser: NewDeserialiser[T]) {
    roundTrip(serialiser, deserialiser, value)
  }
  
  private def typeOf[T](value : T) : String = value match {
    case b : Byte => "byte"
    case s : Short => "short"
    case i : Int => "int"
    case l : Long => "long"
    case f : Float => "float"
    case d : Double => "double"
    case c : Char => "char"
    case z : Boolean => "boolean"
    case () => "unit"
    case o :AnyRef => o.getClass.getName
  }
}