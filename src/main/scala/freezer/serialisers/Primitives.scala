package freezer.serialisers
import freezer.collection.ArrayView

object Primitives {
  val serialiseByte : NewSerialiser[Byte] = { b : Byte =>
    Array[Byte](b)
  }
  
  val deserialiseByte : NewDeserialiser[Byte] = { (a : ArrayView[Byte]) =>
    (a(0),a.drop(1))
  }
  
  val serialiseShort : NewSerialiser[Short] = { s : Short =>
    Array((s >>> 8).toByte,
           s.toByte)
  }
  
  val deserialiseShort : NewDeserialiser[Short] = { (a : ArrayView[Byte]) =>
    if(a.length>=2) {
      val s = ((a(0) <<  8) |
	       (a(1) & 0xFF)).toShort
	  (s,a.drop(2))
    }
    else {
      (0,a) 
    }
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
  
  val serialiseLong : NewSerialiser[Long] = { l : Long =>
    Array(
      (l >>> 56).toByte,
      (l >>> 48).toByte,
      (l >>> 40).toByte,
      (l >>> 32).toByte,
      (l >>> 24).toByte,
      (l >>> 16).toByte,
      (l >>> 8).toByte,
      l.toByte)
  }
  
  val deserialiseLong : NewDeserialiser[Long] = { (a :ArrayView[Byte]) =>
    if (a.length >= 8) {
      val i: Long = (a(0) << 56L) |
        ((a(1) & 0xFF).toLong << 48L) |
        ((a(2) & 0xFF).toLong << 40L) |
        ((a(3) & 0xFF).toLong << 32L) |
        ((a(4) & 0xFF).toLong << 24L) |
        ((a(5) & 0xFF).toLong << 16L) |
        ((a(6) & 0xFF).toLong << 8L) |
        (a(7) & 0xFF)

      new LoadResult(i, a.drop(8))
    } else {
      new LoadResult(0L, a)
    }  
  }
  
  val serialiseFloat : NewSerialiser[Float] = { f : Float =>
    serialiseInt(java.lang.Float.floatToRawIntBits(f))
  }
  
  val deserialiseFloat : NewDeserialiser[Float] = { (a :ArrayView[Byte]) =>
    val (i, rest) = deserialiseInt(a)
    (java.lang.Float.intBitsToFloat(i),rest)
  }
  
  val serialiseDouble : NewSerialiser[Double] = {d: Double =>
    serialiseLong(java.lang.Double.doubleToRawLongBits(d))
  }
  
  val deserialiseDouble : NewDeserialiser[Double] = { (a:ArrayView[Byte]) =>
    val (l, rest) = deserialiseLong(a)
    (java.lang.Double.longBitsToDouble(l),rest)
  }
  
  val serialiseBoolean : NewSerialiser[Boolean] = { f : Boolean =>
    f match {
      case true => serialiseInt(1)
      case false => serialiseInt(0)
    }
  }
  
  val deserialiseBoolean : NewDeserialiser[Boolean] = { (a :ArrayView[Byte]) =>
    val (i, rest) = deserialiseInt(a)
    i match {
      case 1 => (true,rest)
      case 0 => (false,rest)
    }
  }
  
  val serialiseChar : NewSerialiser[Char] = { c : Char=>
    serialiseShort(c.toShort)
  }
  
  val deserialiseChar : NewDeserialiser[Char] = { (a :ArrayView[Byte]) =>
    val (s, rest) = deserialiseShort(a)
    (s.toChar,rest)
  }

  val serialiseUnit : NewSerialiser[Unit] = { c : Unit=>
    Array()
  }
  
  val deserialiseUnit : NewDeserialiser[Unit] = { (a :ArrayView[Byte]) =>
    ((),a)
  }
  
}