package freezer.serialisers
import freezer.collection.ArrayView

object Primitives {
  val serialiseByte : NewSerialiser[Byte] = { b : Byte =>
    Array[Byte](b)
  }
  
  val deserialiseByte : NewDeserialiser[Byte] = { (a : ArrayView[Byte]) =>
    (a(0),a.drop(1))
  }
  
  val serialiseInt : NewSerialiser[Int] = { i : Int =>
    Array((i >>> 24).toByte, 
          (i >>> 16).toByte, 
          (i >>> 8).toByte,
           i.toByte)
  }
  
  val deserialiseInt : NewDeserialiser[Int] = { ( a : ArrayView[Byte]) =>
    if(a.length>=4) {
      val i = 
	      (a(0) << 24) |
	      ((a(1) & 0xFF) << 16 ) |
	      ((a(2) & 0xFF) <<  8) |
	       (a(3) & 0xFF)
	  (i,a.drop(4))
    }
    else {
      (0,a) 
    }
  }
  
  val serialise : NewSerialiser[AnyVal] = {  v:AnyVal=> v match {
      case b : Byte => serialiseByte(b)
      case i : Int => serialiseInt(i)
    }
  }
  
//  val deserialise : NewDeserialiser[AnyVal] = { (a : ArrayView[Byte]) => 
//    t match {
//      case "byte" => deserialiseByte(t,a)
//      case "int" => deserialiseInt(t,a)
//    }
//  }
}