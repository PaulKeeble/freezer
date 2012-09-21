package freezer.serialisers

import scala.annotation.switch
import Primitives._

object DefaultSerialisers {
  private val serialisePrimitives : PartialFunction[Any,Array[Byte]] = {
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
  
  val serialiseAnyVal : NewSerialiser[AnyVal] = (v:AnyVal) => {
    serialisePrimitives(v)
  }
  
  val deserialiseAnyVal: Selector[AnyVal] = {
    import Primitives._
    val f : Selector[AnyVal] = (t: String) => {
      t match {
        case "byte" => deserialiseByte
        case "short" => deserialiseShort
        case "int" => deserialiseInt
        case "long" => deserialiseLong
        case "float" => deserialiseFloat
        case "double" => deserialiseDouble
        case "char" => deserialiseChar
        case "boolean" => deserialiseBoolean
        case "unit" => deserialiseUnit
      }
    }
    f
  }
  
  def composeDeserialiser(deserialiseObjects : Selector[AnyRef]) :Selector[Any] = {
    deserialiseAnyVal.orElse(deserialiseObjects)
  }
  
  def composeSerialiser(serialiseObjects : NewSerialiser[AnyRef]) : NewSerialiser[Any] = {
    (v:Any) => {
      if(serialisePrimitives.isDefinedAt(v))
        serialiseAnyVal(v.asInstanceOf[AnyVal])
      else
        serialiseObjects(v.asInstanceOf[AnyRef])
    }
  }
}