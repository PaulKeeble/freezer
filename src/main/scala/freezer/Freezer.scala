package freezer
import java.lang.reflect.Field
import scala.collection.mutable.SynchronizedSet
import scala.collection._
import freezer.obj.TypeRegister
import freezer.serialisers.IntSerialiser
import freezer.serialisers.TypeRegisterSerialiser
import scala.collection.mutable.ArrayBuilder
import freezer.obj.ObjectGraph
import freezer.serialisers.Serialiser
import freezer.serialisers.DefaultInlineSerialisers
import freezer.serialisers.LoadResult
import freezer.obj.FreezeProcess
import freezer.obj.ObjectIndex
import freezer.obj.SystemReference
import freezer.serialisers.ObjectIndexSerialiser
import freezer.serialisers.ObjectReferenceSerialiser

class Freezer {
  def freeze(obj : AnyRef) :Array[Byte] = {
    if (obj == null) return Array()
    
    val objGraph = new ObjectGraph(obj)
    
    new FreezeProcess().freeze(objGraph)
  }
  
  def unfreeze(frozen : Array[Byte]) : AnyRef = {
    if(frozen.length==0) return null
    
    val registerResult = new TypeRegisterSerialiser().load(frozen)
    val typeRegister = registerResult.result
    
    val indexResult = new ObjectIndexSerialiser(registerResult.result).load(registerResult.remaining)
    val objectIndex = indexResult.result
    
    var remainingBytes = indexResult.remaining

    val deserialiseObject:PartialFunction[(String,Array[Byte]),LoadResult[Any]]= { a => a match {
      	case (_,bytes)  => new ObjectReferenceSerialiser(objectIndex).load(bytes)
      }
    }
    
    val deserialiseFunction = DefaultInlineSerialisers.deserialisers.orElse(deserialiseObject).lift
    
    objectIndex.foreach { o =>
      val obj = o.obj
      obj.getClass().getDeclaredFields().foreach { f =>
        val read = fromBytes(f.getType(),remainingBytes,deserialiseFunction)
        
        f.setAccessible(true)
        f.set(obj,read.result)
        remainingBytes = read.remaining
      }
      
    }
    objectIndex.first.obj
  }
  
  private def fromBytes(clazz : Class[_],bytes : Array[Byte],deserialiseFunction : Function[(String,Array[Byte]),Option[LoadResult[Any]]]) : LoadResult[Any]  = {
    deserialiseFunction(clazz.getName(),bytes) match {
      case Some(x) => x
      case None => throw new RuntimeException("Type " + clazz +" not supported")
    }
  }
}