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
  
  def load(stored : Array[Byte]) : LoadResult[Long] = {
    if(stored.length>=8) {
      val i :Long = (stored(0) << 56L ) |
	      ((stored(1) & 0xFF).toLong << 48L ) |
	      ((stored(2) & 0xFF).toLong << 40L ) |
	      ((stored(3) & 0xFF).toLong << 32L ) |
	      ((stored(4) & 0xFF).toLong << 24L ) |
	      ((stored(5) & 0xFF).toLong << 16L ) |
	      ((stored(6) & 0xFF).toLong <<  8L) |
	       (stored(7) & 0xFF)
	    
	    new LoadResult(i,stored.drop(8))
    }
    else {
      new LoadResult(0L,stored) 
    }
  }
}