package freezer.serialisers
import freezer.obj.ObjectIndex
import freezer.collection.ArrayView

class ObjectReferenceSerialiser(val index:ObjectIndex) extends Serialiser[AnyRef]{
  private val intSerialiser = new IntSerialiser
  private val NULL = -1
  
  def store(obj : AnyRef) : Array[Byte] = {
    if(obj==null)
      return intSerialiser.store(NULL)
      
    index.indexOf(obj) match {
      case Some(x) => intSerialiser.store(x)
      case None => throw new ObjectLookupException("Unable to find object, programming error")
    }
  }
  
  def load(stored : ArrayView[Byte]) : LoadResult[AnyRef] = {
    val loadResult = intSerialiser.load(stored)
    if(loadResult.result == NULL)
      return new LoadResult(null,loadResult.remaining)
    
    val objIndex = loadResult.result
    
    index.atIndex(objIndex) match {
      case Some(obj) =>  new LoadResult(obj.obj,loadResult.remaining)
      case None => throw new ObjectLookupException("Unable to reference existing object, programming error")
    }
  }
}