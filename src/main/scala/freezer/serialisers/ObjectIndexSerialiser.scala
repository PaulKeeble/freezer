package freezer.serialisers
import freezer.obj.ObjectIndex
import freezer.obj.TypeRegister
import scala.collection.mutable.ArrayBuilder
import freezer.obj.SystemReference

class ObjectIndexSerialiser(private val types :TypeRegister) extends Serialiser[ObjectIndex]{
  val intSerialiser = new IntSerialiser
  
  def store(index:ObjectIndex) : Array[Byte] = {
    
    val builder = ArrayBuilder.make[Byte]
    
    builder++= intSerialiser.store(index.size)
    
    val bytes = index.objs.toArray.map { entry =>objToBytes(entry) }
    
    bytes.foldLeft(builder) { _ ++= _ }
    
    builder.result
  }
  
  private def objToBytes(entry:AnyRef) = {
    val clazz = entry.getClass()
      val typeIndex = types.indexOf(clazz)
      typeIndex match {
        case Some(i) => intSerialiser.store(i)
        case None => throw new RuntimeException("Programming error, type" + clazz+ " not in registry")
      }
  }
  
  def load(stored: Array[Byte]) : LoadResult[ObjectIndex] = {
    val entriesRead = intSerialiser.load(stored)
    val numberEntries = entriesRead.result
    var entriesStored = entriesRead.remaining
    
    val index = new ObjectIndex
    
    index++= 0.until(numberEntries).map { _ =>
      val readResult = intSerialiser.load(entriesStored)
      entriesStored = readResult.remaining
      
      types.atIndex(readResult.result) match {
        case Some(clazz) => SystemReference(newObject(clazz))
        case None => throw new RuntimeException("Programming error types have not been initialised correctly")
      }
    }
    
    new LoadResult(index,entriesStored)
  }
  
  private def newObject(clazz: Class[_]) :AnyRef = {
    val empty = Array[Class[_]]()
    val emptyObj = Array[AnyRef]()
    try {
	    val con = clazz.getDeclaredConstructor(empty:_*)
	    con.setAccessible(true)
	    con.newInstance(emptyObj:_*).asInstanceOf[AnyRef]
    } catch {
      case _ => throw new NoDefaultConstructorException(clazz+" does not have a no argument constructor, can't create instance")
    }
  }
}