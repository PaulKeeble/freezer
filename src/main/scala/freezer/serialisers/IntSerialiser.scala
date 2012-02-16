package freezer.serialisers

class IntSerialiser extends Serialiser[Int] {
  def store(i : Int) : Array[Byte] = {
    
    Array(((i & 0xFF000000)>> 24).toByte, 
         ((i & 0x00FF0000)>> 16).toByte, 
         ((i & 0x0000FF00) >> 8).toByte,
         (i & 0xFF).toByte)
  }
  
  def load(stored : Array[Byte]) : LoadResult[Int] = stored match {
    case Array(b0,b1,b2,b3,_*) => {
	    val i = (stored(0) << 24 & 0xFF000000) | 
	    (stored(1) << 16 & 0x00FF0000) | 
	    (stored(2) <<  8 & 0x0000FF00) | 
	    stored(3) & 0x000000FF
	    
	    new LoadResult(i,stored.drop(4))
    }
    case _ => new LoadResult(0,stored) 
  }
}