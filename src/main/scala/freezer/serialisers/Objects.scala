package freezer.serialisers

import freezer.obj.ObjectIndex
import freezer.collection.ArrayView

object Objects {
  private val NULL = -1

  def referenceSerialiser(index: ObjectIndex): NewSerialiser[AnyRef] = {
    def toBytes(obj: AnyRef): Array[Byte] = {
      if (obj == null)
        return Primitives.serialiseInt(NULL)

      index.indexOf(obj) match {
        case Some(x) => Primitives.serialiseInt(x)
        case None => throw new ObjectLookupException("Unable to find object, programming error")
      }
    }
    toBytes
  }

  def referenceDeserialiser(index: ObjectIndex): NewDeserialiser[AnyRef] = {
    def fromBytes(stored: ArrayView[Byte]): LoadResult[AnyRef] = {
      val (objId,remainingBytes) = Primitives.deserialiseInt(stored)
      if (objId == NULL)
        return new LoadResult(null, remainingBytes)

      index.atIndex(objId) match {
        case Some(obj) => new LoadResult(obj.obj, remainingBytes)
        case None => throw new ObjectLookupException("Unable to reference existing object, programming error")
      }
    }
    fromBytes
  }
  
  def selector(objDeserialiser : NewDeserialiser[AnyRef]) : Selector[AnyRef] = {
    {
      case x : String => objDeserialiser
    }
  }
}