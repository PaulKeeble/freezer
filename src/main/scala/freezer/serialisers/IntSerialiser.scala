package freezer.serialisers

class IntSerialiser extends Serialiser[Int] {
  def store(i : Int) : Array[Byte] = {
    Array((i >>> 24).toByte, 
          (i >>> 16).toByte, 
          (i >>> 8).toByte,
           i.toByte)
  }
  
  def load(stored : Array[Byte]) : LoadResult[Int] = {
    if(stored.length>=4) {
      val i = 
	      (stored(0) << 24) |
	      ((stored(1) & 0xFF) << 16 ) |
	      ((stored(2) & 0xFF) <<  8) |
	       (stored(3) & 0xFF)
	  new LoadResult(i,stored.drop(4))
    }
    else {
      new LoadResult(0,stored) 
    }
  }
}