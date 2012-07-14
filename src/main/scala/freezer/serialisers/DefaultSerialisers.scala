package freezer.serialisers

import scala.annotation.switch

object DefaultSerialisers {
  type Selector[T] = PartialFunction[String,NewDeserialiser[T]]
  
  def serialiseAnyVal(v:AnyVal) = {
    import Primitives._
    v match {
      case b: Byte => serialiseByte(b)
      case s: Short => serialiseShort(s)
      case i: Int => serialiseInt(i)
      case l: Long => serialiseLong(l)
      case f: Float => serialiseFloat(f)
      case d: Double => serialiseDouble(d)
      case c: Char => serialiseChar(c)
      case b: Boolean => serialiseBoolean(b)
      case () => serialiseUnit(())
    }
  }
  
  def deserialiseAnyVal: Selector[AnyVal] = {
    val f : Selector[AnyVal] = (t: String) => {
      t match {
        case "byte" => Primitives.deserialiseByte
        case "int" => Primitives.deserialiseInt
      }
    }
    f
  }
  
  def deserialiseAnySelector :Selector[Any] = {
    val f : Selector[Any] = (t:String) => {
      t match {
        case "byte" => deserialiseAnyVal("byte")
        case "int" => deserialiseAnyVal("int")
      }
    }
    f
  }
}