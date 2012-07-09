package freezer

import freezer.collection.ArrayView

package object serialisers {
	type NewSerialiser[-T] = Function1[T,Array[Byte]]
	
	type NewDeserialiser[+T] = Function1[ArrayView[Byte],(T,ArrayView[Byte])]
}