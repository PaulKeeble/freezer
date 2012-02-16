package freezer.serialisers
import freezer.obj.TypeRegister
import scala.collection.mutable.ArrayBuilder

class TypeRegisterSerialiser extends Serialiser[TypeRegister] {
  private val stringSerialiser = new StringSerialiser
  private val intSerialiser = new IntSerialiser
  
  def store(typeRegister : TypeRegister) :Array[Byte]= {
    val builder = ArrayBuilder.make[Byte]
    
   builder ++= intSerialiser.store(typeRegister.size)
   
    val classNamesAsBytes = typeRegister.map{ c => stringSerialiser.store(c.getName()) }
    classNamesAsBytes.foldLeft(builder) { _ ++= _}
    
    builder.result
  }
  
  def load(stored : Array[Byte]) : LoadResult[TypeRegister] = {
    val sizeResult = intSerialiser.load(stored)
    val numberEntries = sizeResult.result
    
    var remainingBytes =sizeResult.remaining
    
    val typeRegister = new TypeRegister
    for(i <-0 until numberEntries) {
      val read = stringSerialiser.load(remainingBytes)
      
      typeRegister += Class.forName(read.result)
      remainingBytes = read.remaining
    }
    
    new LoadResult(typeRegister,remainingBytes)
  }
}