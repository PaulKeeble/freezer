package freezer
import java.lang.reflect.Field
import scala.collection.mutable.SynchronizedSet
import scala.collection._
import freezer.obj.TypeRegister
import freezer.serialisers.IntSerialiser
import freezer.serialisers.TypeRegisterSerialiser
import scala.collection.mutable.ArrayBuilder
import freezer.obj.ObjectGraph

class Freezer {
  
  def freeze(obj : AnyRef) : Array[Byte] = {
    if (obj == null) return Array()
    
    val builder = ArrayBuilder.make[Byte]
    builder++=types(obj)
    
    builder ++=serialiseObject(obj).flatten
    builder.result()
  }
  
  def types(obj : AnyRef) ={
    val typeRegister = new TypeRegister()
    val objGraph = new ObjectGraph(obj)
    typeRegister ++= objGraph.getAll.map {_.getClass()}
    
    new TypeRegisterSerialiser().store(typeRegister)
  }
  
  private def serialiseField(f : Field, obj : Any) : Array[Byte] = {
    f.setAccessible(true)
    val value = f.get(obj)
    val asInt = value.asInstanceOf[Int]
    val bytes = new IntSerialiser().store(asInt)
    bytes
  }
  
  private def serialiseObject(root: AnyRef) : Array[Array[Byte]] = {
    val fields = root.getClass().getDeclaredFields()
    fields.map {f => serialiseField(f,root)}
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