package freezer.serialisers

class ShortSerialiser extends Serialiser[Short]{
  def store(i : Short) : Array[Byte] = {
    Array(
      (i >>> 8).toByte,
       i.toByte
    )
  }
  
  def load(stored:Array[Byte]) : LoadResult[Short] = stored match  {
    case Array(b0,b1,_*) => {
	    val i : Short = 
	      ((b0 << 8) |
	       b1 & 0xFF).shortValue()
	    
	    new LoadResult(i,stored.drop(4))
    }
    case _ => new LoadResult(0.shortValue,stored) 
  }
}