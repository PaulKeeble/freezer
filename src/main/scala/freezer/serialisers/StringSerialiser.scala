package freezer.serialisers
import freezer.collection.ArrayView

class StringSerialiser extends Serialiser[String]{
  val encoding="UTF-8"
  private val ints = new IntSerialiser
  
  def store(s : String) : Array[Byte] = {
    ints.store(s.length()) ++  s.getBytes(encoding).toList
  }
  
  def load(stored: ArrayView[Byte]) : LoadResult[String] = {
    val lengthLoaded = ints.load(stored)
    
    val (strBytes,remainingMessage) = lengthLoaded.remaining.splitAt(lengthLoaded.result)
    
    val decoded = new String(strBytes,encoding)
    new LoadResult(decoded,remainingMessage)
  }
}