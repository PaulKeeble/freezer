package freezer.serialisers

class LongSerialiser extends Serialiser[Long]{
  def store(i :Long) : Array[Byte] = {
        Array(
          (i >>> 56).toByte,
          (i >>> 48).toByte,
          (i >>> 40).toByte,
          (i >>> 32).toByte,
          (i >>> 24).toByte, 
          (i >>> 16).toByte, 
          (i >>> 8).toByte,
           i.toByte)
  }
  
  def load(stored : Array[Byte]) : LoadResult[Long] = stored match {
    case Array(b0,b1,b2,b3,b4,b5,b6,b7,_*) => {
	    val i :Long = (b0 << 56L ) |
	      (b1 & 0xFF << 48L ) |
	      (b2 & 0xFF << 40L ) |
	      (b3 & 0xFF << 32L ) |
	      (b4 & 0xFF << 24L ) |
	      (b5 & 0xFF << 16L ) |
	      (b6 & 0xFF <<  8L) |
	       b7 & 0xFF
	    
	    new LoadResult(i,stored.drop(8))
    }
    case _ => new LoadResult(0L,stored) 
  }
}