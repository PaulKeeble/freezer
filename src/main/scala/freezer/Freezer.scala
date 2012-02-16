package freezer
import java.lang.reflect.Field
import scala.collection.mutable.SynchronizedSet
import scala.collection._
import freezer.obj.TypeRegister
import freezer.serialisers.IntSerialiser
import freezer.serialisers.TypeRegisterSerialiser
import scala.collection.mutable.ArrayBuilder

class Freezer {
  
  def freeze(obj : AnyRef) : Array[Byte] = {
    if (obj == null) return Array()
    
    val builder = ArrayBuilder.make[Byte]
    
    val typeRegister = new TypeRegister()
    typeRegister += obj.getClass()
    builder++= new TypeRegisterSerialiser().store(typeRegister)
    
    val fields = obj.getClass().getDeclaredFields()
    val allFields = fields.map {f => toBytes(f,obj)}
    
    builder ++=allFields.flatten
    builder.result()
  }
  
  private def toBytes(f : Field, obj : Any) : Array[Byte] = {
    f.setAccessible(true)
    val value = f.get(obj)
    val asInt = value.asInstanceOf[Int]
    val bytes = new IntSerialiser().store(asInt)
    bytes
  }
  
  def unfreeze(frozen : Array[Byte]) : AnyRef = {
    if(frozen.length==0) return null
    
    val registerResult = new TypeRegisterSerialiser().load(frozen)
    
    val typeRegister = registerResult.result
    
    val objects = typeRegister.map{ c => 
      val obj = c.newInstance()
      obj.asInstanceOf[AnyRef]
    }.toList
    
    
    var remainingBytes = registerResult.remaining
    objects.foreach { o =>
      o.getClass().getDeclaredFields().foreach { f =>
        val read = fromBytes(remainingBytes)
        
        f.setAccessible(true)
        f.set(o,read.result)
        remainingBytes = read.remaining
      }
      
    }
    
    objects.head
  }
  
  private def fromBytes(bytes : Array[Byte])  = {
    new IntSerialiser().load(bytes)
  }
}