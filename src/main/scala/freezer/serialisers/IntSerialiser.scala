package freezer.serialisers

class IntSerialiser extends Serialiser[Int] {
  def store(i : Int) : Array[Byte] = {
    Array((i >>> 24).toByte, 
          (i >>> 16).toByte, 
          (i >>> 8).toByte,
           i.toByte)
  }
  
  def load(stored : Array[Byte]) : LoadResult[Int] = stored match {
    case Array(b0,b1,b2,b3,_*) => {
	    val i = 
	      (b0 << 24) |
	      ((b1 & 0xFF) << 16 ) |
	      ((b2 & 0xFF) <<  8) |
	       (b3 & 0xFF)
	    
	    new LoadResult(i,stored.drop(4))
    }
    case _ => new LoadResult(0,stored) 
  }
}