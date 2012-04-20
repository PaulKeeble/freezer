package freezer.obj
import freezer.serialisers.DefaultInlineSerialisers
import java.lang.reflect.Field
import freezer.serialisers.TypeRegisterSerialiser
import freezer.serialisers.ObjectIndexSerialiser
import scala.collection.mutable.ArrayBuilder
import freezer.serialisers.ObjectReferenceSerialiser

class FreezeProcess() {
  private val typeSerialiser = new TypeRegisterSerialiser()
  
  def freeze(objGraph: ObjectGraph): Array[Byte] = {
    val builder = ArrayBuilder.make[Byte]

    builder ++= typeSerialiser.store(objGraph.types)
    builder ++= new ObjectIndexSerialiser(objGraph.types).store(objGraph.index)
    builder ++= serialiseObjects(objGraph)
    builder.result()
  }

  private def serialiseObjects(objGraph: ObjectGraph): Array[Byte] = {
    val index = objGraph.index

    val serialisationFunction = objectSerialisationFunciton(index)

    val bytesForObjects = objGraph.index.toArray.map { sr => serialiseObject(sr.obj, serialisationFunction) }
    bytesForObjects.flatten
  }

  private def objectSerialisationFunciton(index: ObjectIndex) = {
    val objectRefFunction: PartialFunction[Any, Array[Byte]] = {
      a: Any =>
        a match {
          case b: AnyRef => new ObjectReferenceSerialiser(index).store(b)
        }
    }
    val serialisationFunction = DefaultInlineSerialisers.serialisers.orElse({ objectRefFunction }).lift
    serialisationFunction
  }

  private def serialiseObject(obj: AnyRef, serialisationFunction: Any => Option[Array[Byte]]): Array[Byte] = {
    val fields = obj.getClass().getDeclaredFields()
    fields.map(field => serialiseField(serialisationFunction,field, obj)).flatten
  }
  
  def serialiseField(serialisationFunction: Any => Option[Array[Byte]],f: Field, obj: Any): Array[Byte] = {
    f.setAccessible(true)
    val value = f.get(obj)

    serialisationFunction(value) match {
      case Some(x) => x
      case None => throw new RuntimeException("Not supported yet")
    }
  }
}