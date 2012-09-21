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
import freezer.obj.ObjectIndex
import freezer.obj.SystemReference
import freezer.serialisers.ObjectIndexSerialiser
import freezer.serialisers.ObjectReferenceSerialiser
import freezer.collection.ArrayView
import freezer.serialisers.DefaultSerialisers
import freezer.serialisers.Objects
import freezer.serialisers.Selector

class Freezer {
  def freeze(obj : AnyRef) :Array[Byte] = {
    if (obj == null) return Array()
    
    val objGraph = new ObjectGraph(obj)

    val serialisationFunction = DefaultSerialisers.composeSerialiser(Objects.referenceSerialiser(objGraph.index))

    objGraph.freeze(serialisationFunction)
  }
  
  def unfreeze(frozen : Array[Byte]) : AnyRef = {
    if(frozen.length==0) return null
    
    val registerResult = new TypeRegisterSerialiser().load(frozen)
    val typeRegister = registerResult.result
    
    val indexResult = new ObjectIndexSerialiser(typeRegister).load(registerResult.remaining)
    val objectIndex = indexResult.result
    
    var remainingBytes = indexResult.remaining
    
    val deserialiseFunction = DefaultSerialisers.composeDeserialiser(Objects.selector(Objects.referenceDeserialiser(objectIndex)))
    
    objectIndex.foreach { o =>
      val obj = o.obj
      obj.getClass().getDeclaredFields().foreach { f =>
      	val read = deserialiseFunction(f.getType().getName())(remainingBytes)
        
        f.setAccessible(true)
        f.set(obj,read._1)
        remainingBytes = read._2
      }
    }
    objectIndex.head.obj
  }
}